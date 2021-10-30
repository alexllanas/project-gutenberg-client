package com.example.simplebookwormapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.simplebookwormapp.adapters.BookRecyclerAdapter;
import com.example.simplebookwormapp.util.VerticalSpacingItemDecoration;

public class BookListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private BookRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mRecyclerView = findViewById(R.id.book_list);

        initRecyclerView();

        mAdapter.displayBookCategories();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void initRecyclerView() {
        mAdapter = new BookRecyclerAdapter(initGlide());
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(30);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
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