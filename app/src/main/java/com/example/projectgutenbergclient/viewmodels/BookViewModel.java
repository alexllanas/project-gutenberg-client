package com.example.projectgutenbergclient.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.projectgutenbergclient.models.ContentPath;
import com.example.projectgutenbergclient.repositories.BookRepository;
import com.example.projectgutenbergclient.util.Resource;

import javax.inject.Inject;

public class BookViewModel extends AndroidViewModel {

    private final BookRepository bookRepository;
    private final MediatorLiveData<Resource<ContentPath>> bookContent = new MediatorLiveData<>();

    private boolean cancelRequest;
    private boolean isPerformingQuery;
    private String url;
    private long bookId;

    @Inject
    public BookViewModel(@NonNull Application application, BookRepository bookRepository) {
        super(application);
        this.bookRepository = bookRepository;
    }

    public LiveData<Resource<ContentPath>> getBookContent() {
        return bookContent;
    }

    public void searchBookContent(String url, long bookId, Context context) {
        this.url = url;
        this.bookId = bookId;

        executeSearch(context);
    }

    private void executeSearch(Context context) {
        cancelRequest = false;
        isPerformingQuery = true;

        final LiveData<Resource<ContentPath>> repositorySource = bookRepository.searchBookContent(url, bookId, context);
        bookContent.addSource(repositorySource, new Observer<Resource<ContentPath>>() {
            @Override
            public void onChanged(Resource<ContentPath> contentPathResource) {
                if (!cancelRequest) {
                    processResource(repositorySource, contentPathResource);
                } else {
                    bookContent.removeSource(repositorySource);
                }
            }
        });
    }

    private void processResource(LiveData<Resource<ContentPath>> repositorySource, Resource<ContentPath> contentPathResource) {
        if (contentPathResource != null) {
            processSuccessOrError(repositorySource, contentPathResource);
            bookContent.setValue(contentPathResource);
        } else {
            bookContent.removeSource(repositorySource);
        }
    }

    private void processSuccessOrError(LiveData<Resource<ContentPath>> repositorySource, Resource<ContentPath> contentPathResource) {
        if (contentPathResource.status == Resource.Status.SUCCESS) {
            isPerformingQuery = false;
            bookContent.removeSource(repositorySource);
        } else if (contentPathResource.status == Resource.Status.ERROR) {
            isPerformingQuery = false;
            bookContent.removeSource(repositorySource);
        } else if (contentPathResource.status == Resource.Status.LOADING) {
            isPerformingQuery = true;
        }
    }

    public void cancelSearchRequest() {
        if (isPerformingQuery) {
            cancelRequest = true;
            isPerformingQuery = false;
        }
    }
}
