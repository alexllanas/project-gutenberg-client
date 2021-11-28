package com.example.projectgutenbergclient.di;

import com.example.projectgutenbergclient.di.BookList.BookListModule;
import com.example.projectgutenbergclient.di.BookList.BookListScope;
import com.example.projectgutenbergclient.di.BookList.BookListViewModelsModule;
import com.example.projectgutenbergclient.ui.BookListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @BookListScope
    @ContributesAndroidInjector(modules = {BookListViewModelsModule.class, BookListModule.class})
    abstract BookListActivity contributeBookListActivity();
}
