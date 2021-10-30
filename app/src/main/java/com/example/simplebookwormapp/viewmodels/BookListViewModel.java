package com.example.simplebookwormapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.simplebookwormapp.repositories.BookRepository;

public class BookListViewModel extends AndroidViewModel {

    private BookRepository bookRepository;

    public BookListViewModel(@NonNull Application application) {
        super(application);
    }


}
