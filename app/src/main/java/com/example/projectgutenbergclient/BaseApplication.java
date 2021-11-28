package com.example.projectgutenbergclient;

import com.example.projectgutenbergclient.di.DaggerAppComponent;
import com.example.projectgutenbergclient.util.MyDebugTree;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import de.hdodenhof.circleimageview.BuildConfig;
import timber.log.Timber;

public class BaseApplication extends DaggerApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new MyDebugTree());
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
