package com.example.simplebookwormapp.requests;

import androidx.lifecycle.LiveData;

import com.example.simplebookwormapp.requests.responses.ApiResponse;
import com.example.simplebookwormapp.requests.responses.BookSearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApi {

    @GET("books")
    LiveData<ApiResponse<BookSearchResponse>> searchBook(
        @Query("search") String query,
        @Query("page") String page
    );

    @GET("books")
    LiveData<ApiResponse<BookSearchResponse>> searchTopic(
            @Query("topic") String topic,
            @Query("page") String page
    );

}
