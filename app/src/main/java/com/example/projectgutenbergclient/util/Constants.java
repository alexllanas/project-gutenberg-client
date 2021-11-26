package com.example.projectgutenbergclient.util;

public class Constants {

    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 2; // 2 seconds
    public static final int WRITE_TIMEOUT = 2; // 2 seconds

    public static final String QUERY_EXHAUSTED = "No more results.";

    public static final String BASE_URL = "https://gutendex.com/";

    public static final String[] DEFAULT_BOOK_CATEGORIES =
            {"Adventure", "Mystery", "Children", "Horror", "Drama", "Comedies", "Romance", "Philosophy"};

    public static final String[] DEFAULT_BOOK_CATEGORIES_IMAGES =
            {"adventure", "mystery", "children", "horror", "drama", "comedies", "romance", "philosophy"};


}
