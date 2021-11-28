package com.example.projectgutenbergclient.di.BookList;

import androidx.lifecycle.ViewModel;

import com.example.projectgutenbergclient.di.ViewModelKey;
import com.example.projectgutenbergclient.viewmodels.BookListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class BookListViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(BookListViewModel.class)
    public abstract ViewModel bindBookListViewModel(BookListViewModel viewModel);
}
