package com.example.simplebookwormapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.simplebookwormapp.AppExecutors;
import com.example.simplebookwormapp.models.Book;
import com.example.simplebookwormapp.persistence.BookDao;
import com.example.simplebookwormapp.persistence.BookDatabase;
import com.example.simplebookwormapp.requests.RetrofitService;
import com.example.simplebookwormapp.requests.responses.ApiResponse;
import com.example.simplebookwormapp.requests.responses.BookSearchResponse;
import com.example.simplebookwormapp.util.NetworkBoundResource;
import com.example.simplebookwormapp.util.Resource;

import java.util.List;

import timber.log.Timber;

public class BookRepository {

    private static BookRepository INSTANCE;
    private BookDao bookDao;

    public static BookRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository(context);
        }
        return INSTANCE;
    }

    private BookRepository(Context context) {
        bookDao = BookDatabase.getInstance(context).getBookDao();
    }

    /**
     * Query local cache for book query, if not present, fetch from network and then save to cache.
     *
     * FYI: query will always be fetched from network and cache will be refreshed everytime a query
     * is made.
     *
     * @param query
     * @param pageNumber
     * @return
     */
    public LiveData<Resource<List<Book>>> searchBooksApi(final String query, final int pageNumber) {
        return new NetworkBoundResource<List<Book>, BookSearchResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull BookSearchResponse item) {
                if (item.getBooks() != null) {
                    saveResponse(item);
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
                return RetrofitService.getBookApi().searchBook(query, String.valueOf(pageNumber));
            }
        }.getAsLiveData();
    }

    private void saveResponse(@NonNull BookSearchResponse item) {
        Book[] books = new Book[item.getBooks().size()];

        int index = 0;
        for (long rowId : bookDao.insertBooks((Book[]) (item.getBooks().toArray(books)))) {
            if (rowId == -1) {
                Timber.d("saveCallResult: CONFLICT... This book is already in the cache");
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

}
