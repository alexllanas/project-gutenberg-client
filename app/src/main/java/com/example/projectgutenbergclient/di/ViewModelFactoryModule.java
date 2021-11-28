package com.example.projectgutenbergclient.di;

import androidx.lifecycle.ViewModelProvider;

import com.example.projectgutenbergclient.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

}
