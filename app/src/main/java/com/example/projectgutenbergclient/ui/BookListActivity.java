package com.example.projectgutenbergclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.projectgutenbergclient.R;
import com.example.projectgutenbergclient.adapters.BookRecyclerAdapter;
import com.example.projectgutenbergclient.adapters.OnBookListener;
import com.example.projectgutenbergclient.databinding.ActivityBookListBinding;
import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.models.Formats;
import com.example.projectgutenbergclient.repositories.BookRepository;
import com.example.projectgutenbergclient.util.Constants;
import com.example.projectgutenbergclient.util.Resource;
import com.example.projectgutenbergclient.util.VerticalSpacingItemDecoration;
import com.example.projectgutenbergclient.viewmodels.BookListViewModel;
import com.example.projectgutenbergclient.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.BuildConfig;
import timber.log.Timber;

//public class BookListActivity extends BaseActivity implements OnBookListener {
public class BookListActivity extends DaggerAppCompatActivity implements OnBookListener {

    private ActivityBookListBinding binding;

    private BookListViewModel mBookListViewModel;
    private BookRecyclerAdapter mAdapter;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBookListViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(BookListViewModel.class);

        initRecyclerView();
        initSearchView();

        setSupportActionBar(binding.toolbar);
    }


    @Override
    protected void onStart() {
        super.onStart();
        subscribeObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayLoading(false);
    }

    @Override
    public void onBackPressed() {
        if (mBookListViewModel.getViewState().getValue() == BookListViewModel.ViewState.CATEGORIES)
            super.onBackPressed();
        else {
            mBookListViewModel.cancelSearchRequest();
            mBookListViewModel.setViewCategories();
        }
    }

    @Override
    public void onBookClick(int position) {
        displayLoading(true);

        Intent intent = new Intent(this, BookActivity.class);
        if (mAdapter.getSelectedBook(position) != null) {
            Book book = mAdapter.getSelectedBook(position);
            if (book != null) {
                Formats formats = book.getFormats();
                if (formats != null) {
                    String url = null;
                    if (formats.getText_plain_utf_8() != null) {
                        url = formats.getText_plain_utf_8();
                    } else if (formats.getText_plain_ascii() != null) {
                        url = formats.getText_plain_ascii();
                    }
                    if (url != null) {
                        Timber.d(url);
                        intent.putExtra("url", url);
                        intent.putExtra("id", book.getId());
                        startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    public void onCategoryClick(String category) {
        searchBookApi(category, true);
    }

    private void subscribeObservers() {
        mBookListViewModel.getBooks().observe(this, listResource -> {
            if (listResource != null) {
                if (listResource.data != null) {
                    processResourceByStatus(listResource);
                }
            }
        });

        mBookListViewModel.getViewState().observe(this, viewState -> {
            if (viewState != null) {
                switch (viewState) {
                    case CATEGORIES: {
                        displayCategories();
                    }
                }
            }
        });
    }

    private void initSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBookApi(query, false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initRecyclerView() {
        ViewPreloadSizeProvider<String> viewPreloadSizeProvider = new ViewPreloadSizeProvider<>();

        mAdapter = new BookRecyclerAdapter(this, requestManager, viewPreloadSizeProvider);
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(30);
        binding.bookList.addItemDecoration(itemDecoration);
        binding.bookList.setLayoutManager(new LinearLayoutManager(this));

        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<>(
                Glide.with(this),
                mAdapter,
                viewPreloadSizeProvider,
                32);
        binding.bookList.addOnScrollListener(preloader);

        binding.bookList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!binding.bookList.canScrollVertically(1) && mBookListViewModel.getViewState().getValue() == BookListViewModel.ViewState.BOOKS) {
                    mBookListViewModel.searchNextPage();
                }
            }
        });
        binding.bookList.setAdapter(mAdapter);
    }

    private void displayCategories() {
        binding.bookList.scrollToPosition(0);
        mAdapter.displayBookCategories();
        binding.searchView.setQuery("", false);
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
                mAdapter.hideLoading();
                assert listResource.data != null;
                mAdapter.setBooks(listResource.data);
                break;
            }
        }
    }

    private void processErrorResource(Resource<List<Book>> listResource) {
        mAdapter.hideLoading();
        assert listResource.data != null;
        mAdapter.setBooks(listResource.data);

        if (BuildConfig.DEBUG) {
            Toast.makeText(BookListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
        }

        if (listResource.message.equals(Constants.QUERY_EXHAUSTED)) {
            mAdapter.setQueryExhausted();
        }
    }

    private void processLoadingResource() {
        if (mBookListViewModel.getPageNumber() > 1) {
            // adapter should display loading for pagination
            mAdapter.displayLoading();
        } else {
            // adapter should display only progress bar for loading page 1 data
            mAdapter.displayOnlyLoading();
        }
    }


    private void searchBookApi(String query, boolean searchTopic) {
        binding.bookList.scrollToPosition(0);
        mBookListViewModel.searchBooksApi(query, 1, searchTopic);
        binding.searchView.clearFocus();
    }


    private void displayLoading(boolean displayLoading) {
        if (displayLoading) {
            binding.bookList.setVisibility(View.INVISIBLE);
            showProgressBar(true);
        } else {
            showProgressBar(false);
            binding.bookList.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressBar( boolean visibility) {
        binding.progressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}