package com.example.projectgutenbergclient.di.BookList;

import com.example.projectgutenbergclient.requests.BookApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class BookListModule {

    @BookListScope
    @Provides
    static BookApi provideAuthApi(Retrofit retrofit) {
        return retrofit.create(BookApi.class);
    }
}
