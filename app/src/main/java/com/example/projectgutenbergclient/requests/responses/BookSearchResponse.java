package com.example.projectgutenbergclient.requests.responses;

import androidx.annotation.Nullable;

import com.example.projectgutenbergclient.models.Book;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookSearchResponse {

    @SerializedName("count")
    @Expose()
    private int count;

    @SerializedName("results")
    @Expose()
    private List<Book> books;

    @SerializedName("detail")
    @Expose()
    private String error;


    public int getCount() {
        return count;
    }

    @Nullable
    public List<Book> getBooks() {
        return books;
    }


    public String getError() {
        return error;
    }
}
