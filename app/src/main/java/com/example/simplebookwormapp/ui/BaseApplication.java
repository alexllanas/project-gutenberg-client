package com.example.simplebookwormapp.ui;

import android.app.Application;
import android.os.Build;

import com.example.simplebookwormapp.BuildConfig;

import timber.log.Timber;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
