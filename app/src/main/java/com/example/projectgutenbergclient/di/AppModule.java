package com.example.projectgutenbergclient.di;

import static com.example.projectgutenbergclient.persistence.BookDatabase.DATABASE_NAME;

import android.app.Application;

import androidx.room.Room;

import com.example.projectgutenbergclient.di.BookList.BookListScope;
import com.example.projectgutenbergclient.persistence.BookDao;
import com.example.projectgutenbergclient.persistence.BookDatabase;
import com.example.projectgutenbergclient.persistence.ContentPathDao;
import com.example.projectgutenbergclient.repositories.BookRepository;
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

    @Singleton
    @Provides
    static BookDatabase provideBookDatabase(Application application) {
        return Room.databaseBuilder(
                application,
                BookDatabase.class,
                DATABASE_NAME
        ).build();
    }

    @Singleton
    @Provides
    static BookDao provideBookDao(BookDatabase bookDatabase) {
        return bookDatabase.getBookDao();
    }

    @Singleton
    @Provides
    static ContentPathDao provideContentPathDao(BookDatabase bookDatabase) {
        return bookDatabase.getContentPathDao();
    }

    @Singleton
    @Provides
    static BookRepository provideBookRepository(BookDao bookDao, ContentPathDao contentPathDao) {
        return new BookRepository(bookDao, contentPathDao);
    }
}
