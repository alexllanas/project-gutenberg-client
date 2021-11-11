package com.example.simplebookwormapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.simplebookwormapp.models.Book;
import com.example.simplebookwormapp.repositories.BookRepository;
import com.example.simplebookwormapp.util.Constants;
import com.example.simplebookwormapp.util.Resource;

import java.util.List;

import timber.log.Timber;

public class BookListViewModel extends AndroidViewModel {

    public enum ViewState {CATEGORIES, BOOKS}

    private MutableLiveData<ViewState> viewState;
    private final MediatorLiveData<Resource<List<Book>>> books = new MediatorLiveData<>();
    private final BookRepository bookRepository;

    // extra query info
    private boolean isPerformingQuery;
    private boolean isQueryExhausted;
    private boolean searchTopic;
    private boolean cancelRequest;
    private String query;
    private int pageNumber;

    public BookListViewModel(@NonNull Application application) {
        super(application);
        bookRepository = BookRepository.getInstance(application);
        Timber.d(String.valueOf(isPerformingQuery));
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

    public void searchBooksApi(String query, int pageNumber, boolean searchTopic) {
        if (!isPerformingQuery) {
            if (pageNumber == 0) {
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            this.query = query;
            this.searchTopic = searchTopic;
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
        final LiveData<Resource<List<Book>>> repositorySource = bookRepository.searchBooksApi(query, pageNumber, searchTopic);
        books.addSource(repositorySource, new Observer<Resource<List<Book>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Book>> listResource) {
                Timber.d("inside executeSearch");
                Timber.d(listResource.message);
                processResource(repositorySource, listResource);
            }
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
        if (listResource.status == Resource.Status.SUCCESS) {
            Timber.d("Success");
            isPerformingQuery = false;
            checkIfResourceExhausted(listResource);
            books.removeSource(repositorySource);
        } else if (listResource.status == Resource.Status.ERROR) {
            Timber.d("Error");
            isPerformingQuery = false;
            if (listResource.message.equals(Constants.QUERY_EXHAUSTED)) {
                isQueryExhausted = true;
            }
            books.removeSource(repositorySource);
        }
    }

    private void checkIfResourceExhausted(Resource<List<Book>> listResource) {
        if (listResource.data != null) {
            if (listResource.data.size() == 0) {
                books.setValue(
                        new Resource<>(
                                Resource.Status.ERROR,
                                listResource.data,
                                Constants.QUERY_EXHAUSTED
                        )
                );
                isQueryExhausted = true;
            }
        }
    }

    public void cancelSearchRequest() {
        if (isPerformingQuery) {
            cancelRequest = true;
            isPerformingQuery = false;
            pageNumber = 1;
        }
    }



}
