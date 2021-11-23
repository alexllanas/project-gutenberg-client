package com.example.simplebookwormapp.repositories;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.simplebookwormapp.models.ContentPath;
import com.example.simplebookwormapp.persistence.ContentPathDao;
import com.example.simplebookwormapp.util.AppExecutors;
import com.example.simplebookwormapp.models.Book;
import com.example.simplebookwormapp.persistence.BookDao;
import com.example.simplebookwormapp.persistence.BookDatabase;
import com.example.simplebookwormapp.requests.RetrofitService;
import com.example.simplebookwormapp.requests.responses.ApiResponse;
import com.example.simplebookwormapp.requests.responses.BookSearchResponse;
import com.example.simplebookwormapp.util.NetworkBoundResource;
import com.example.simplebookwormapp.util.Resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.List;

import okhttp3.ResponseBody;
import timber.log.Timber;

public class BookRepository {

    private static BookRepository INSTANCE;
    private BookDao bookDao;
    private ContentPathDao contentPathDao;

    public static BookRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository(context);
        }
        return INSTANCE;
    }

    private BookRepository(Context context) {
        bookDao = BookDatabase.getInstance(context).getBookDao();
        contentPathDao = BookDatabase.getInstance(context).getContentPathDao();
    }

    public LiveData<Resource<ContentPath>> searchBookContent(final String url, long bookId, Context context) {
        return new NetworkBoundResource<ContentPath, ResponseBody>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull ResponseBody item) {
                try {
                    Timber.d("BookID to save in DB: %d", bookId);
                    Timber.d("in saveCallResult");
                    Timber.d("item = %s", item);
                    Timber.d("item.contentLength = %s", item.contentLength());
                    Reader r = item.charStream();
                    int intch;
                    StringBuilder sb = new StringBuilder();
                    while ((intch = r.read()) != -1) {
                        char ch = (char) intch;
                        sb.append(ch);
                    }
//                    Timber.d("data = %s", sb.toString());
                    saveBookContentResponse(sb.toString(), bookId, context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable ContentPath data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<ContentPath> loadFromDb() {
                Timber.d("BookID to load from database: %d", bookId);
                return contentPathDao.getPath(bookId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
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
                Timber.d("loadFromDb called");
                return bookDao.searchBooks(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<BookSearchResponse>> createCall() {
                if (searchTopic) {
                    Timber.d("query: " + query + " page number=" + pageNumber + " searchtopic=" + searchTopic);
                    return RetrofitService.getBookApi().searchTopic(query, String.valueOf(pageNumber));
                } else {
                    Timber.d("query: " + query + " page number=" + pageNumber + " searchtopic=" + searchTopic);
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
                Timber.d("saveCallResult: CONFLICT... This book is already in the cache");
                Timber.d(books[index].getTitle());
                // If book already exists do not insert because timestamp will be overwritten.
                bookDao.updateBook(
                        books[index].getBook_id(),
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
        Timber.d("in writeToFile");
        Timber.d(data);

        String fileName = bookId + ".txt";
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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


//        try {
//            String path = context.getFilesDir().getAbsolutePath() + bookId + ".txt";
//            Timber.d(path);
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(path, Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//
//            ContentPath contentPath = new ContentPath(bookId, path);
//            contentPathDao.insertContentPath(contentPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Timber.e("An error occurred while writing data to file.");
//        }
    }

}
