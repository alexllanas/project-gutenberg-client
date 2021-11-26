package com.example.projectgutenbergclient;

import android.app.Application;

import com.example.projectgutenbergclient.util.MyDebugTree;

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
