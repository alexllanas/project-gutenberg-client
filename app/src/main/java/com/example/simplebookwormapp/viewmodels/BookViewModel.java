package com.example.simplebookwormapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.simplebookwormapp.repositories.BookRepository;
import com.example.simplebookwormapp.util.Resource;

import timber.log.Timber;

public class BookViewModel extends AndroidViewModel {

    private final BookRepository bookRepository;
    private final MediatorLiveData<Resource<String>> bookContent = new MediatorLiveData<>();

    private boolean cancelRequest;
    private boolean isPerformingQuery;

    public BookViewModel(@NonNull Application application) {
        super(application);
        this.bookRepository = BookRepository.getInstance(application);
    }

    public LiveData<Resource<String>> getBookContent() {
        return bookContent;
    }

    public void searchBookContent(String url) {
        cancelRequest = false;
        isPerformingQuery = true;

        final LiveData<Resource<String>> repositorySource = bookRepository.searchBookContent(url);
        bookContent.addSource(repositorySource, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                Timber.d(String.valueOf(stringResource == null));
                Timber.d(String.valueOf(null == stringResource.data));
                processResource(repositorySource, stringResource);
            }
        });

    }

    private void processResource(LiveData<Resource<String>> repositorySource, Resource<String> stringResource) {
        if (!cancelRequest) {
            if (stringResource != null) {
                processSuccessOrError(repositorySource, stringResource);
                bookContent.setValue(stringResource);
            } else {
                bookContent.removeSource(repositorySource);
            }
        } else {
            bookContent.removeSource(repositorySource);
        }
    }

    private void processSuccessOrError(LiveData<Resource<String>> repositorySource, Resource<String> stringResource) {
        if (stringResource.status == Resource.Status.SUCCESS) {
            Timber.d("Success");
            isPerformingQuery = false;
            bookContent.removeSource(repositorySource);
        } else if (stringResource.status == Resource.Status.ERROR) {
            Timber.d("Error");
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
