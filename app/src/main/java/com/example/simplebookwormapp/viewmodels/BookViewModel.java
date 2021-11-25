package com.example.simplebookwormapp.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.simplebookwormapp.models.ContentPath;
import com.example.simplebookwormapp.repositories.BookRepository;
import com.example.simplebookwormapp.util.Resource;

import timber.log.Timber;

public class BookViewModel extends AndroidViewModel {

    private final BookRepository bookRepository;
    private final MediatorLiveData<Resource<ContentPath>> bookContent = new MediatorLiveData<>();

    private boolean cancelRequest;
    private boolean isPerformingQuery;

    public BookViewModel(@NonNull Application application) {
        super(application);
        this.bookRepository = BookRepository.getInstance(application);
    }

    public LiveData<Resource<ContentPath>> getBookContent() {
        return bookContent;
    }

    public void searchBookContent(String url, long bookId, Context context) {
        cancelRequest = false;
        isPerformingQuery = true;

        final LiveData<Resource<ContentPath>> repositorySource = bookRepository.searchBookContent(url, bookId, context);
        bookContent.addSource(repositorySource, new Observer<Resource<ContentPath>>() {
            @Override
            public void onChanged(Resource<ContentPath> contentPathResource) {
                processResource(repositorySource, contentPathResource);
            }
        });

    }

    private void processResource(LiveData<Resource<ContentPath>> repositorySource, Resource<ContentPath> contentPathResource) {
        if (!cancelRequest) {
            if (contentPathResource != null) {
                processSuccessOrError(repositorySource, contentPathResource);
                bookContent.setValue(contentPathResource);
            } else {
                bookContent.removeSource(repositorySource);
            }
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
        }
    }

    public void cancelSearchRequest() {
        if (isPerformingQuery) {
            cancelRequest = true;
            isPerformingQuery = false;
        }
    }
}
