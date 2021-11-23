package com.example.simplebookwormapp;

import android.app.Application;
import android.os.Build;

import com.example.simplebookwormapp.BuildConfig;
import com.example.simplebookwormapp.util.MyDebugTree;

import timber.log.Timber;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new MyDebugTree());
        }
    }
}
