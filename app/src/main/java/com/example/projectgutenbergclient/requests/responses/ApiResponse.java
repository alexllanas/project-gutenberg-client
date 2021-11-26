package com.example.projectgutenbergclient.requests.responses;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Response;

public class ApiResponse<T> {

    public ApiResponse<T> create(Throwable error) {
        return new ApiErrorResponse<>(Objects.equals(error.getMessage(), "") ? error.getMessage() : "Unknown error\nCheck network connection");
    }

    public ApiResponse<T> create(Response<T> response) {
        if (response.isSuccessful()) {
            T body = response.body();
            if (body instanceof BookSearchResponse) {
                if (((BookSearchResponse) body).getCount() == 0) {
                    // query has no results
                    return new ApiErrorResponse<>("No more results.");
                }
            }

            if (body == null || response.code() == 204) { // 204 is empty response
                return new ApiEmptyResponse<>();
            } else {
                return new ApiSuccessResponse<>(body);
            }
        } else {
            String errorMsg = "";
            if (response.code() == 404) {
                // pagination exhausted
                errorMsg = "No more results.";
            } else {
                try {
                    assert response.errorBody() != null;
                    errorMsg = response.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    errorMsg = response.message();
                }
            }
            return new ApiErrorResponse<>(errorMsg);
        }
    }

    /**
     * Generic success response from api
     *
     * @param <T>
     */
    public static class ApiSuccessResponse<T> extends ApiResponse<T> {

        private final T body;

        ApiSuccessResponse(T body) {
            this.body = body;
        }

        public T getBody() {
            return body;
        }

    }

    /**
     * Generic Error response from API
     *
     * @param <T>
     */
    public static class ApiErrorResponse<T> extends ApiResponse<T> {

        private final String errorMessage;

        ApiErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }


    /**
     * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
     */
    public static class ApiEmptyResponse<T> extends ApiResponse<T> {
    }

}
