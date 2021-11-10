package com.example.simplebookwormapp.adapters;

import static java.util.List.*;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.simplebookwormapp.R;
import com.example.simplebookwormapp.models.Author;
import com.example.simplebookwormapp.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final RequestManager requestManager;
    private final OnBookListener onBookListener;
    TextView title, author;
    ImageView image;

    public BookViewHolder(View itemView, OnBookListener onBookListener, RequestManager requestManager) {
        super(itemView);

        this.requestManager = requestManager;
        this.onBookListener = onBookListener;

        title = itemView.findViewById(R.id.book_title);
        author = itemView.findViewById(R.id.book_author);
        image = itemView.findViewById(R.id.book_image);

    }

    public void onBind(Book book) {
        if (book != null) {
            if (book.getFormats() != null) {
                setMediumImage(book);
            }
            title.setText(book.getTitle());
//            author.setText(book.getAuthors().get(0).getName());
            if (book.getAuthors() != null)
                setAuthorsText(book);
        }

    }

    private void setMediumImage(Book book) {
        String imageUrl = book.getFormats().getImage_jpeg();
        if (imageUrl != null) {
            if (imageUrl.contains("small")) {
                imageUrl = imageUrl.replace("small", "medium");
            }
            requestManager
                    .load(imageUrl)
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
