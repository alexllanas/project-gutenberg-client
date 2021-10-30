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

    private static final int CATEGORY_TYPE = 1;

    private List<Book> mBooks;
    private RequestManager requestManager;

    public BookRecyclerAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case CATEGORY_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, requestManager);
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);

        if (itemViewType == CATEGORY_TYPE) {
            ((CategoryViewHolder) holder).onBind(mBooks.get(position));
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
        if(mBooks.get(position).getId() == -1) {
            return CATEGORY_TYPE;
        }
        return 0;
    }

    public void displayBookCategories() {
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
}
