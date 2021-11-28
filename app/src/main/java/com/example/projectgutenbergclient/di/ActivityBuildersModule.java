package com.example.projectgutenbergclient.di;

import com.example.projectgutenbergclient.di.Book.BookModule;
import com.example.projectgutenbergclient.di.Book.BookScope;
import com.example.projectgutenbergclient.di.Book.BookViewModelsModule;
import com.example.projectgutenbergclient.di.BookList.BookListModule;
import com.example.projectgutenbergclient.di.BookList.BookListScope;
import com.example.projectgutenbergclient.di.BookList.BookListViewModelsModule;
import com.example.projectgutenbergclient.ui.BookActivity;
import com.example.projectgutenbergclient.ui.BookListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @BookListScope
    @ContributesAndroidInjector(modules = {BookListViewModelsModule.class, BookListModule.class})
    abstract BookListActivity contributeBookListActivity();

    @BookScope
    @ContributesAndroidInjector(modules = {BookViewModelsModule.class, BookModule.class})
    abstract BookActivity contributeBookActivity();

}
