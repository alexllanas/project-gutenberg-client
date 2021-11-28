package com.example.projectgutenbergclient.di;

import com.example.projectgutenbergclient.di.BookList.BookListScope;
import com.example.projectgutenbergclient.requests.BookApi;
import com.example.projectgutenbergclient.util.Constants;
import com.example.projectgutenbergclient.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static BookApi provideAuthApi(Retrofit retrofit) {
        return retrofit.create(BookApi.class);
    }
}
