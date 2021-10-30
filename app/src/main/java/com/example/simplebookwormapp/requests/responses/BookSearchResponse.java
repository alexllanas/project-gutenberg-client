package com.example.simplebookwormapp.requests.responses;

import com.example.simplebookwormapp.models.Book;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookSearchResponse {

    private int count;

    @SerializedName("next")
    private String nextPage;

    @SerializedName("previous")
    private String previousPage;

    private List<Book> results;
}
