package com.example.simplebookwormapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.simplebookwormapp.models.Book;
import com.example.simplebookwormapp.repositories.BookRepository;
import com.example.simplebookwormapp.util.Resource;

import java.util.List;

public class BookListViewModel extends AndroidViewModel {

    public static final String QUERY_EXHAUSTED = "No more results.";

    public enum ViewState {CATEGORIES, BOOKS}

    private MutableLiveData<ViewState> viewState;
    private final MediatorLiveData<Resource<List<Book>>> books = new MediatorLiveData<>();

    // extra query info
    private boolean isPerformingQuery;
    private boolean isQueryExhausted;
    private boolean cancelRequest;
    private String query;
    private int pageNumber;
    private long requestStartTime;

    private BookRepository bookRepository;

    public BookListViewModel(@NonNull Application application) {
        super(application);
        bookRepository = BookRepository.getInstance(application);
        initViewState();
    }

    private void initViewState() {
        if (viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public LiveData<ViewState> getViewState() {
        return viewState;
    }

    public LiveData<Resource<List<Book>>> getBooks() {
        return books;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setViewCategories() {
        viewState.setValue(ViewState.CATEGORIES);
    }

    public void searchRecipesApi(String query, int pageNumber) {
        if (!isPerformingQuery) {
            if (pageNumber == 0) {
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            this.query = query;
            isQueryExhausted = false;
            executeSearch();
        }
    }

    public void searchNextPage() {
        if (!isQueryExhausted && !isPerformingQuery) {
            pageNumber++;
            executeSearch();
        }
    }

    public void executeSearch() {
        cancelRequest = false;
        isPerformingQuery = true;
        viewState.setValue(ViewState.BOOKS);

        final LiveData<Resource<List<Book>>> repositorySource = bookRepository.searchBooksApi(query, pageNumber);
        books.addSource(repositorySource, listResource -> {
            processResource(repositorySource, listResource);
        });
    }

    private void processResource(LiveData<Resource<List<Book>>> repositorySource, Resource<List<Book>> listResource) {
        if (!cancelRequest) {
            if (listResource != null) {
                processSuccessOrError(repositorySource, listResource);
                books.setValue(listResource);
            } else {
                books.removeSource(repositorySource);
            }
        } else {
            books.removeSource(repositorySource);
        }
    }

    private void processSuccessOrError(LiveData<Resource<List<Book>>> repositorySource, Resource<List<Book>> listResource) {
        isPerformingQuery = false;
        books.removeSource(repositorySource);

        if (listResource.status == Resource.Status.SUCCESS) {
            checkIfResourceExhausted(listResource);
        } else if (listResource.status == Resource.Status.ERROR) {
            if (listResource.message.equals(QUERY_EXHAUSTED)) {
                isQueryExhausted = true;
            }
        }
    }

    private void checkIfResourceExhausted(Resource<List<Book>> listResource) {
        if (listResource.data != null) {
            if (listResource.data.size() == 0) {
                books.setValue(
                        new Resource<>(
                                Resource.Status.ERROR,
                                listResource.data,
                                QUERY_EXHAUSTED
                        )
                );
                isQueryExhausted = true;
            }
        }
    }

    private void cancelSearchRequest() {
        if (isPerformingQuery) {
            cancelRequest = true;
            isPerformingQuery = false;
            pageNumber = 1;
        }
    }

}
