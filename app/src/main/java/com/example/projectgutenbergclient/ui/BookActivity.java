package com.example.projectgutenbergclient.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.example.projectgutenbergclient.BuildConfig;
import com.example.projectgutenbergclient.R;
import com.example.projectgutenbergclient.databinding.ActivityBookBinding;
import com.example.projectgutenbergclient.models.ContentPath;
import com.example.projectgutenbergclient.util.Resource;
import com.example.projectgutenbergclient.viewmodels.BookViewModel;
import com.example.projectgutenbergclient.viewmodels.ViewModelProviderFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

//public class BookActivity extends BaseActivity {
public class BookActivity extends DaggerAppCompatActivity {

    private ActivityBookBinding binding;
    private BookViewModel mBookViewModel;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        mBookViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(BookViewModel.class);

//        showProgressBar(true);
        binding.progressBar.setVisibility(View.VISIBLE);

        subscribeObservers();

//        if (savedInstanceState == null) {
        searchBookContent();
//        }
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
                showProgressBar(true);
                break;
            }
            case ERROR: {
                showProgressBar(false);
                Timber.d("in error");
                if (BuildConfig.DEBUG) {
                    binding.bookContent.setText(R.string.content_network_error);
                }
                break;
            }
            case SUCCESS: {
                if (contentPathResource.data != null) {
                    Timber.d("in success");
                    Timber.d("reading book content from file path");
                    String content = readFromFile(contentPathResource.data.getPath_id());
                    Timber.d("done reading from file");
                    binding.bookContent.setText(content);
                    showProgressBar(false);
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

            while (br.ready()) {
                String line = br.readLine();
                sb.append(line + "\n");
            }

//            while ((text = br.readLine()) != null) {
//                sb.append(text).append("\n");
//            }
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

//    private String readFromFile(long book_id) {
//        FileInputStream fis = null;
//        String filename = book_id + ".txt";
//
//        try {
//            fis = openFileInput(filename);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String text;
//
//            while ((text = br.readLine()) != null) {
//                sb.append(text).append("\n");
//            }
//            return sb.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return "";
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBookViewModel.cancelSearchRequest();
    }

    public void showProgressBar(boolean visibility) {
        binding.progressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}
