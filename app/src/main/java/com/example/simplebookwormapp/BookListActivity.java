package com.example.simplebookwormapp;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.simplebookwormapp.adapters.BookRecyclerAdapter;

public class BookListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private BookRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        mRecyclerView = findViewById(R.id.book_list);
        mAdapter = new BookRecyclerAdapter(initGlide());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.displayBookCategories();
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide
                .with(this)
                .setDefaultRequestOptions(options);
    }
}