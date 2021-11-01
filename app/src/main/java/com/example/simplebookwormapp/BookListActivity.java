package com.example.simplebookwormapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.simplebookwormapp.adapters.BookRecyclerAdapter;
import com.example.simplebookwormapp.adapters.OnBookListener;
import com.example.simplebookwormapp.models.Book;
import com.example.simplebookwormapp.util.Constants;
import com.example.simplebookwormapp.util.Resource;
import com.example.simplebookwormapp.util.VerticalSpacingItemDecoration;
import com.example.simplebookwormapp.viewmodels.BookListViewModel;

import java.util.List;

public class BookListActivity extends BaseActivity implements OnBookListener {

    private BookListViewModel mBookListViewModel;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private BookRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mRecyclerView = findViewById(R.id.book_list);

        mBookListViewModel = ViewModelProviders.of(this).get(BookListViewModel.class);

        initRecyclerView();
        subscribeObservers();

        mAdapter.displayBookCategories();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void subscribeObservers() {
        mBookListViewModel.getBooks().observe(this, listResource -> {
            if (listResource != null && listResource.data != null) {
                processResourceByStatus(listResource);
            }
        });
    }

    private void processResourceByStatus(@NonNull Resource<List<Book>> listResource) {
        switch (listResource.status) {
            case LOADING: {
                processLoadingResource();
                break;
            }
            case ERROR: {
                processErrorResource(listResource);
                break;
            }
            case SUCCESS: {
                // hide loading in adapter
                // set resource data (books) in adapter
                break;
            }
        }
    }

    private void processErrorResource(Resource<List<Book>> listResource) {
        // adapter should hide loading
        // set resource data (book list) to adapter
        // show optional Toast with error message

        // check to see if results have been exhausted
        if (listResource.message.equals(Constants.QUERY_EXHAUSTED)) {
            // tell adapter to display no results view at end of recyclerview
        }
    }

    private void processLoadingResource() {
        if (mBookListViewModel.getPageNumber() > 1) {
            // adapter should display loading for pagination
        } else {
            // adapter should display only progress bar for loading page 1 data
        }
    }

    private void initRecyclerView() {
        mAdapter = new BookRecyclerAdapter(this, initGlide());
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(30);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                if (!mRecyclerView.canScrollVertically(1)
//                && )
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void searchBookApi(String query) {
        mRecyclerView.scrollToPosition(0);
        mBookListViewModel.searchRecipesApi(query, 1);
        mSearchView.clearFocus();
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide
                .with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    public void onBookClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {
        searchBookApi(category);
    }
}