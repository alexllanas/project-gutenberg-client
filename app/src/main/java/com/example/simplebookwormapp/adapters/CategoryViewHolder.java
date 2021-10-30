package com.example.simplebookwormapp.adapters;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.simplebookwormapp.R;
import com.example.simplebookwormapp.models.Book;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView categoryImage;
    TextView categoryTitle;
    RequestManager requestManager;

    public CategoryViewHolder(@NonNull View itemView, RequestManager requestManager) {
        super(itemView);
        this.requestManager = requestManager;

        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);
    }

    public void onBind(Book book) {
        Uri path = Uri.parse("android.resource://com.example.simplebookwormapp/drawable/" + book.getFormats().getImage_jpeg());
        requestManager
                .load(path)
                .into(categoryImage);
        categoryTitle.setText(book.getTitle());
    }

    @Override
    public void onClick(View view) {

    }
}
