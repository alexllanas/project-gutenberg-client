package com.example.simplebookwormapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.simplebookwormapp.BuildConfig;
import com.example.simplebookwormapp.R;
import com.example.simplebookwormapp.models.ContentPath;
import com.example.simplebookwormapp.util.Resource;
import com.example.simplebookwormapp.viewmodels.BookViewModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        mBookViewModel.getBookContent().observe(this, new Observer<Resource<ContentPath>>() {
            @Override
            public void onChanged(Resource<ContentPath> contentPathResource) {
                if (contentPathResource != null) {
                    Timber.d(contentPathResource.status.toString());
                    Timber.i("content path resource data == null: %b", (contentPathResource.data == null));

                    if (contentPathResource.data != null) {
                        Timber.d(contentPathResource.data.getPath());
                        processResourceByStatus(contentPathResource);
                    }
                }
            }
        });
    }

    private void processResourceByStatus(Resource<ContentPath> contentPathResource) {
        switch (contentPathResource.status) {
            case LOADING: {
                Timber.d("in LOADING");
                showProgressBar(true);
                break;
            }
            case ERROR: {
                Timber.d("in ERROR");
                showProgressBar(false);
                if (BuildConfig.DEBUG) {
                    mBookContent.setText("No content.");
                }
                break;
            }
            case SUCCESS: {
                Timber.d("in SUCCESS");
                showProgressBar(false);
                String content = readFromFile(this, contentPathResource.data.getPath_id());
                Timber.d("CONTENT= " + content);
                mBookContent.setText(content);
                break;
            }
        }
    }

    private void searchBookContent() {
        if (getIntent().getExtras() != null) {
            String url = getIntent().getStringExtra("url");
            long book_id = getIntent().getLongExtra("id", -1);
            if (url != null || book_id != -1) {
                Timber.d(url);
                mBookViewModel.searchBookContent(url, book_id, this);
            }
        }
    }

    private String readFromFile(Context context, long book_id) {
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
            Timber.d("in readFromFile");
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


//        String data = "";
//
//        Timber.d("READING FROM FILE");
//        Timber.d(getFilesDir().toString());
//        try {
//            String path = getFilesDir().toString() + "/" + book_id + ".txt";
//            InputStream inputStream = context.openFileInput(path);
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String output = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ((output = bufferedReader.readLine()) != null) {
//                    stringBuilder.append("\n").append(output);
//                }
//                inputStream.close();
//                data = stringBuilder.toString();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Timber.e("An error occurred reading from a file.");
//        }
//        return data;
        return "";
    }
}
