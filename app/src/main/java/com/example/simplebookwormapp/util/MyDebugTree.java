package com.example.simplebookwormapp.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import timber.log.Timber;

public class MyDebugTree extends Timber.DebugTree {
    @Nullable
    @Override
    protected String createStackElementTag(@NonNull StackTraceElement element) {
        return String.format("%s@%s:%s",
                super.createStackElementTag(element),
                element.getMethodName(),
                element.getLineNumber());

    }
}
