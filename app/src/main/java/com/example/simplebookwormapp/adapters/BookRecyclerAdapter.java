package com.example.simplebookwormapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.simplebookwormapp.R;
import com.example.simplebookwormapp.models.Book;
import com.example.simplebookwormapp.models.Formats;
import com.example.simplebookwormapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class BookRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LOADING_TYPE = 1;
    private static final int CATEGORY_TYPE = 2;
    private static final int BOOK_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Book> mBooks;
    private OnBookListener mOnBookListener;
    private final RequestManager requestManager;

    public BookRecyclerAdapter(OnBookListener onBookListener, RequestManager requestManager) {
        this.mOnBookListener = onBookListener;
        this.requestManager = requestManager;
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
                return new BookViewHolder(view, mOnBookListener, requestManager);
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
        if (mBooks.get(position).getBook_id() == -1) {
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
        List<Book> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_BOOK_CATEGORIES.length; i++) {
            Book book = new Book();
            book.setTitle(Constants.DEFAULT_BOOK_CATEGORIES[i]);
            Formats formats = new Formats();
            formats.setImage_jpeg(Constants.DEFAULT_BOOK_CATEGORIES_IMAGES[i]);
            book.setFormats(formats);
            book.setBook_id(-1);
            categories.add(book);
        }
        mBooks = categories;
        notifyDataSetChanged();
    }

    public void displayOnlyLoading() {
        clearBooksList();
        Book book = new Book();
        book.setTitle("LOADING");
        mBooks.add(book);
        notifyDataSetChanged();
    }

    public void displayLoading() {
        if (mBooks == null)
            mBooks = new ArrayList<>();
        if (!isLoading()) {
            Book book = new Book();
            book.setTitle("LOADING");
            mBooks.add(book);
            notifyDataSetChanged();
        }
    }

    public void hideLoading() {
        if (isLoading()) {
            if (mBooks.get(0).getTitle().equals("LOADING")) {
                mBooks.remove(0);
            } else if (mBooks.get(mBooks.size() - 1).equals("LOADING")) {
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
        mBooks = books;
        notifyDataSetChanged();
    }

    public void setQueryExhausted() {
        hideLoading();
        Book exhaustedBook = new Book();
        exhaustedBook.setTitle("EXHAUSTED");
        mBooks.add(exhaustedBook);
        notifyDataSetChanged();
    }
}
