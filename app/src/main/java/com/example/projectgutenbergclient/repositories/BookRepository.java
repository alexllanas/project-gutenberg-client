package com.example.projectgutenbergclient.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.projectgutenbergclient.models.ContentPath;
import com.example.projectgutenbergclient.persistence.ContentPathDao;
import com.example.projectgutenbergclient.util.AppExecutors;
import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.persistence.BookDao;
import com.example.projectgutenbergclient.persistence.BookDatabase;
import com.example.projectgutenbergclient.requests.RetrofitService;
import com.example.projectgutenbergclient.requests.responses.ApiResponse;
import com.example.projectgutenbergclient.requests.responses.BookSearchResponse;
import com.example.projectgutenbergclient.util.NetworkBoundResource;
import com.example.projectgutenbergclient.util.Resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import timber.log.Timber;

public class BookRepository {

    private final BookDao bookDao;
    private final ContentPathDao contentPathDao;


    @Inject
    public BookRepository(BookDao bookDao, ContentPathDao contentPathDao) {
        this.bookDao = bookDao;
        this.contentPathDao = contentPathDao;
    }

    public LiveData<Resource<ContentPath>> searchBookContent(final String url, long bookId, Context context) {
        return new NetworkBoundResource<ContentPath, ResponseBody>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull ResponseBody item) {
                try {
                    Reader r = item.charStream();
                    int intch;
                    StringBuilder sb = new StringBuilder();
                    while ((intch = r.read()) != -1) {
                        char ch = (char) intch;
                        sb.append(ch);
                    }
                    saveBookContentResponse(sb.toString(), bookId, context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable ContentPath data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<ContentPath> loadFromDb() {
                Timber.d("loading book path from db");
                return contentPathDao.getPath(bookId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
                Timber.d("fetching book from network");
                return RetrofitService.getBookApi().searchBookContent(url);
            }
        }.getAsLiveData();
    }

    private void saveBookContentResponse(String item, long bookId, Context context) {
        writeToFile(item, bookId, context);

    }

    /**
     * Query local cache for book query, if not present, fetch from network and then save to cache.
     * <p>
     * FYI: query will always be fetched from network and cache will be refreshed everytime a query
     * is made.
     *
     * @param query
     * @param pageNumber
     * @return
     */
    public LiveData<Resource<List<Book>>> searchBooksApi(final String query, final int pageNumber, boolean searchTopic) {
        return new NetworkBoundResource<List<Book>, BookSearchResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull BookSearchResponse item) {
                if (item.getBooks() != null) {
                    saveBookSearchResponse(item);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Book> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Book>> loadFromDb() {
                return bookDao.searchBooks(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<BookSearchResponse>> createCall() {
                Timber.d("fetching from network");
                if (searchTopic) {
                    return RetrofitService.getBookApi().searchTopic(query, String.valueOf(pageNumber));
                } else {
                    return RetrofitService.getBookApi().searchBook(query, String.valueOf(pageNumber));
                }
            }
        }.getAsLiveData();
    }

    private void saveBookSearchResponse(@NonNull BookSearchResponse item) {
        Book[] books = new Book[item.getBooks().size()];

        int index = 0;
        for (long rowId : bookDao.insertBooks(item.getBooks().toArray(books))) {
            if (rowId == -1) {
                // If book already exists do not insert because timestamp will be overwritten.
                bookDao.updateBook(
                        books[index].getId(),
                        books[index].getTitle(),
                        books[index].getAuthors(),
                        books[index].getFormats(),
                        books[index].getSubjects()
                );
            }
            index++;
        }
    }

    private void writeToFile(String data, long bookId, Context context) {
        String fileName = bookId + ".txt";
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            ContentPath contentPath = new ContentPath(bookId, fileName);
            contentPathDao.insertContentPath(contentPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
