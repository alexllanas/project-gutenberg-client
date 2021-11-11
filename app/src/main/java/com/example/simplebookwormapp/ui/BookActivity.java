package com.example.simplebookwormapp.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.simplebookwormapp.BuildConfig;
import com.example.simplebookwormapp.R;
import com.example.simplebookwormapp.util.Resource;
import com.example.simplebookwormapp.viewmodels.BookViewModel;

import timber.log.Timber;

public class BookActivity extends BaseActivity {
    private TextView mBookContent;
    private BookViewModel mBookViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        mBookContent = findViewById(R.id.book_content);

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        subscribeObservers();
        searchBookContent();
    }

    private void subscribeObservers() {
        mBookViewModel.getBookContent().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                if (stringResource != null) {
                    Timber.d(stringResource.status.toString());

                    if (stringResource.data != null) {
                        processResourceByStatus(stringResource);
                    }
                }
            }
        });
    }

    private void processResourceByStatus(Resource<String> stringResource) {
        switch (stringResource.status) {
            case LOADING: {
                showProgressBar(true);
                break;
            }
            case ERROR: {
                showProgressBar(false);
                if (BuildConfig.DEBUG) {
                    mBookContent.setText("No content.");
                }
                break;
            }
            case SUCCESS: {
                showProgressBar(false);
                mBookContent.setText(stringResource.data);
                break;
            }
        }
    }

    private void searchBookContent() {
        if (getIntent().getExtras() != null) {
            String url = getIntent().getStringExtra("url");
            if (url != null) {
                Timber.d(url);
                mBookViewModel.searchBookContent(url);
            }
        }
    }
}
