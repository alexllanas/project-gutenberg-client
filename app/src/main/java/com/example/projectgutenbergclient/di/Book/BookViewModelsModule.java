package com.example.projectgutenbergclient.di.Book;

import androidx.lifecycle.ViewModel;

import com.example.projectgutenbergclient.di.ViewModelKey;
import com.example.projectgutenbergclient.viewmodels.BookListViewModel;
import com.example.projectgutenbergclient.viewmodels.BookViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class BookViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(BookViewModel.class)
    public abstract ViewModel bindBookListViewModel(BookViewModel viewModel);
}
