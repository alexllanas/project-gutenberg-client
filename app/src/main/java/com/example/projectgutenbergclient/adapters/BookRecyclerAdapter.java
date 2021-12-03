package com.example.projectgutenbergclient.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.projectgutenbergclient.R;
import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.models.Formats;
import com.example.projectgutenbergclient.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListPreloader.PreloadModelProvider<String> {

    private static final int LOADING_TYPE = 1;
    private static final int CATEGORY_TYPE = 2;
    private static final int BOOK_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Book> mBooks;
    private final OnBookListener mOnBookListener;
    private final RequestManager requestManager;
    private final ViewPreloadSizeProvider<String> viewPreloadSizeProvider;

    public BookRecyclerAdapter(OnBookListener onBookListener, RequestManager requestManager, ViewPreloadSizeProvider<String> viewPreloadSizeProvider) {
        this.mOnBookListener = onBookListener;
        this.requestManager = requestManager;
        this.viewPreloadSizeProvider = viewPreloadSizeProvider;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case LOADING_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            }
            case BOOK_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_list_item, parent, false);
                return new BookViewHolder(view, mOnBookListener, requestManager, viewPreloadSizeProvider);
            }
            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exhausted_list_item, parent, false);
                return new ExhaustedViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, mOnBookListener, requestManager);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);

        if (itemViewType == CATEGORY_TYPE) {
            ((CategoryViewHolder) holder).onBind(mBooks.get(position));
        } else if (itemViewType == BOOK_TYPE) {
            ((BookViewHolder) holder).onBind(mBooks.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mBooks != null) {
            return mBooks.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mBooks.get(position).getId() == -1) {
            return CATEGORY_TYPE;
        } else if (mBooks.get(position).getTitle().equals("LOADING")) {
            return LOADING_TYPE;
        } else if (mBooks.get(position).getTitle().equals("EXHAUSTED")) {
            return EXHAUSTED_TYPE;
        } else {
            return BOOK_TYPE;
        }
    }

    public Book getSelectedBook(int position) {
        if (mBooks != null) {
            if (mBooks.size() > 0) {
                return mBooks.get(position);
            }
        }
        return null;
    }

    public void displayBookCategories() {
        clearBooksList();
        List<Book> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_BOOK_CATEGORIES.length; i++) {
            Book book = new Book();
            book.setTitle(Constants.DEFAULT_BOOK_CATEGORIES[i]);
            Formats formats = new Formats();
            formats.setImage_jpeg(Constants.DEFAULT_BOOK_CATEGORIES_IMAGES[i]);
            book.setFormats(formats);
            book.setId(-1);
            categories.add(book);
        }
        mBooks = categories;
        notifyDataSetChanged();
    }

    public void displayOnlyLoading() {
        clearBooksList();
        appendItemTypeBook("LOADING");
        notifyDataSetChanged();
    }

    public void displayLoading() {
        if (mBooks == null)
            mBooks = new ArrayList<>();
        if (!isLoading()) {
            appendItemTypeBook("LOADING");
            notifyDataSetChanged();
        }
    }

    public void hideLoading() {
        if (isLoading()) {
            if (mBooks.get(0).getTitle().equals("LOADING")) {
                mBooks.remove(0);

            } else if (mBooks.get(mBooks.size() - 1).getTitle().equals("LOADING")) {
                mBooks.remove(mBooks.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mBooks != null) {
            if (mBooks.size() > 0) {
                if (mBooks.get(mBooks.size() - 1).getTitle().equals("LOADING")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clearBooksList() {
        if (mBooks == null) {
            mBooks = new ArrayList<>();
        } else {
            mBooks.clear();
        }
        notifyDataSetChanged();
    }


    public void setBooks(List<Book> books) {
        if (books.isEmpty()) {
            mBooks = books;
        } else {
            mBooks.addAll(books.subList(mBooks.size(), books.size()));
        }
        notifyDataSetChanged();

    }

    public void setQueryExhausted() {
        hideLoading();
        appendItemTypeBook("EXHAUSTED");
        notifyDataSetChanged();
    }

    private void appendItemTypeBook(String type) {
        Book typeBook = new Book();
        typeBook.setTitle(type);
        mBooks.add(typeBook);
    }

    @NonNull
    @Override
    public List<String> getPreloadItems(int position) {
        String url = "";
        if (mBooks.get(position).getFormats() != null) {
            url = mBooks.get(position).getFormats().getImage_jpeg();
            if (url == null || TextUtils.isEmpty(url)) {
                return Collections.emptyList();
            }
        }
        return Collections.singletonList(url);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull String item) {
        return requestManager.load(item);
    }


}
