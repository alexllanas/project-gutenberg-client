package com.example.projectgutenbergclient.di.BookList;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectgutenbergclient.R;
import com.example.projectgutenbergclient.requests.BookApi;

import dagger.Module;
import dagger.Provides;

@Module
public class BookListModule {

    @BookListScope
    @Provides
    RequestManager provideRequestManager(Application application) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide
                .with(application)
                .setDefaultRequestOptions(options);
    }

}
