package com.example.projectgutenbergclient.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectgutenbergclient.BuildConfig;
import com.example.projectgutenbergclient.R;
import com.example.projectgutenbergclient.models.ContentPath;
import com.example.projectgutenbergclient.util.Resource;
import com.example.projectgutenbergclient.viewmodels.BookViewModel;
import com.example.projectgutenbergclient.viewmodels.ViewModelProviderFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import timber.log.Timber;

public class BookActivity extends BaseActivity {
    private TextView mBookContent;
    private BookViewModel mBookViewModel;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        mBookContent = findViewById(R.id.book_content);

        mBookViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(BookViewModel.class);

        showProgressBar(true);

        subscribeObservers();
        searchBookContent();
    }

    private void subscribeObservers() {
        mBookViewModel.getBookContent().observe(this, new Observer<Resource<ContentPath>>() {
            @Override
            public void onChanged(Resource<ContentPath> contentPathResource) {
                if (contentPathResource != null) {
                    processResourceByStatus(contentPathResource);
                }
            }
        });
    }

    private void processResourceByStatus(Resource<ContentPath> contentPathResource) {
        Timber.d("status = " + contentPathResource.status.toString());
        switch (contentPathResource.status) {
            case LOADING: {
                Timber.d("in loading");
//                showProgressBar(true);
                break;
            }
            case ERROR: {
                showProgressBar(false);
                Timber.d("in error");
                if (BuildConfig.DEBUG) {
                    mBookContent.setText(R.string.content_network_error);
                }
                break;
            }
            case SUCCESS: {
                showProgressBar(false);
                if (contentPathResource.data != null) {
                    Timber.d("in success");
                    Timber.d("reading book content from file path");
                    String content = readFromFile(contentPathResource.data.getPath_id());
                    Timber.d("done reading from file");
                    mBookContent.setText(content);
                    break;
                }
            }
        }
    }

    private void searchBookContent() {
        if (getIntent().getExtras() != null) {
            String url = getIntent().getStringExtra("url");
            long book_id = getIntent().getLongExtra("id", -1);
            if (url != null || book_id != -1) {
//                if (url.endsWith("zip")) {
//                    Timber.d(url);
//                    url = url.replace("zip", "txt");
//                    Timber.d(url);
//                }
                mBookViewModel.searchBookContent(url, book_id, this);
            }
        }
    }

    private String readFromFile(long book_id) {
        FileInputStream fis = null;
        String filename = book_id + ".txt";

        try {
            fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBookViewModel.cancelSearchRequest();
    }
}
