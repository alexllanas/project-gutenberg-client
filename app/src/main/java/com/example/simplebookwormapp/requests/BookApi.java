package com.example.simplebookwormapp.requests;

import androidx.lifecycle.LiveData;

import com.example.simplebookwormapp.requests.responses.ApiResponse;
import com.example.simplebookwormapp.requests.responses.BookSearchResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

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

    @GET
    LiveData<ApiResponse<ResponseBody>> searchBookContent(@Url String url);
}
