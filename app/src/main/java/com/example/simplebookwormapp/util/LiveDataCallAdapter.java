package com.example.simplebookwormapp.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.simplebookwormapp.R;
import com.example.simplebookwormapp.requests.responses.ApiResponse;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {

    private Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @NonNull
    @Override
    public Type responseType() {
        return responseType;
    }

    @NonNull
    @Override
    public LiveData<ApiResponse<R>> adapt(@NonNull Call<R> call) {
        return new LiveData<ApiResponse<R>>(){
            @Override
            protected void onActive() {
                super.onActive();
                final ApiResponse apiResponse = new ApiResponse();
                if (!call.isExecuted()) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(@NonNull Call<R> call, @NonNull Response<R> response) {
                            postValue(apiResponse.create(response));
                        }

                        @Override
                        public void onFailure(@NonNull Call<R> call, @NonNull Throwable t) {
                            postValue(apiResponse.create(t));
                        }
                    });
                }
            }
        };
    }
}
