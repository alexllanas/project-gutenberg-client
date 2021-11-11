package com.example.simplebookwormapp.adapters;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.simplebookwormapp.R;
import com.example.simplebookwormapp.models.Author;
import com.example.simplebookwormapp.models.Book;

import java.util.ArrayList;

import timber.log.Timber;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final RequestManager requestManager;
    private final ViewPreloadSizeProvider viewPreloadSizeProvider;
    private final OnBookListener onBookListener;
    TextView title, author;
    ImageView image;

    public BookViewHolder(View itemView, OnBookListener onBookListener, RequestManager requestManager, ViewPreloadSizeProvider viewPreloadSizeProvider) {
        super(itemView);

        this.requestManager = requestManager;
        this.viewPreloadSizeProvider = viewPreloadSizeProvider;
        this.onBookListener = onBookListener;

        title = itemView.findViewById(R.id.book_title);
        author = itemView.findViewById(R.id.book_author);
        image = itemView.findViewById(R.id.book_image);

        itemView.setOnClickListener(this);
    }

    public void onBind(Book book) {
        if (book != null) {
            if (book.getFormats() != null) {
                setImage(book);
            }

            title.setText(book.getTitle());
            if (book.getAuthors() != null)
                setAuthorsText(book);
        }
        viewPreloadSizeProvider.setView(image);
    }

    private void setImage(Book book) {
        String imageUrl = book.getFormats().getImage_jpeg();
        if (imageUrl != null) {
            String finalImageUrl = imageUrl;
            Timber.e("LOADING IMAGE URL ----> " + imageUrl);
            requestManager
                    .load(imageUrl)
                    .timeout(6000)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Timber.e(e);
                            Timber.e("BAD URL: " + finalImageUrl);
                            e.logRootCauses("bookviewholder");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(image);
        }
    }

    private void setAuthorsText(Book book) {
        ArrayList<Author> authors = (ArrayList<Author>) book.getAuthors();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            if (i == authors.size() - 1) {
                sb.append(authors.get(i).getName());
            } else {
                sb.append(authors.get(i).getName() + "; ");
            }
        }
        author.setText(sb);
    }

    @Override
    public void onClick(View v) {
        onBookListener.onBookClick(getAdapterPosition());
    }
}
