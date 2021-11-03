package com.example.simplebookwormapp.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors INSTANCE;

    public static AppExecutors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppExecutors();
        }
        return INSTANCE;
    }

    private final Executor mDiskIO = Executors.newSingleThreadExecutor();

    private final Executor mMainThreadExecutor = new MainThreadExecutor();


    public Executor diskIO(){
        return mDiskIO;
    }

    public Executor mainThread(){
        return mMainThreadExecutor;
    }


    private static class MainThreadExecutor implements Executor{

        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
