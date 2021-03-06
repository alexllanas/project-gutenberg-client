package com.example.projectgutenbergclient.requests;

import static com.example.projectgutenbergclient.util.Constants.CONNECTION_TIMEOUT;
import static com.example.projectgutenbergclient.util.Constants.READ_TIMEOUT;
import static com.example.projectgutenbergclient.util.Constants.WRITE_TIMEOUT;

import com.example.projectgutenbergclient.util.Constants;
import com.example.projectgutenbergclient.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static OkHttpClient client = new OkHttpClient.Builder()

            // establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            .retryOnConnectionFailure(false)

            .build();

    private static final Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
//                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static BookApi bookApi = retrofit.create(BookApi.class);

    public static BookApi getBookApi() {
        return bookApi;
    }
}
