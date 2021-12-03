package com.example.projectgutenbergclient.util;

import com.example.projectgutenbergclient.models.Author;
import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.models.Formats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class TestUtil {

    public static final long TIMESTAMP_1 = new GregorianCalendar(2000, 0, 1).getTime().getTime();
    public static final long TIMESTAMP_2 = new GregorianCalendar(1991, 10, 26).getTime().getTime();

    public static final ArrayList<Author> TEST_AUTHORS_1 = new ArrayList<>(Arrays.asList(new Author("Wilde"),
            new Author("Sachar"),
            new Author("Martin"))
    );
    public static final ArrayList<Author> TEST_AUTHORS_2 = new ArrayList<>(Arrays.asList(new Author("Tolkien"),
            new Author("Joyce"),
            new Author("Wallace"))
    );

    public static final ArrayList<String> TEST_SUBJECTS_1 = new ArrayList<>(Arrays.asList("Mystery", "Drama", "Horror"));
    public static final ArrayList<String> TEST_SUBJECTS_2 = new ArrayList<>(Arrays.asList("Comedy", "Memoir", "Eastern Philosophy"));

    public static final Formats TEST_FORMATS_1 = new Formats("image", "epub", "rdf", "html", "mobipocket", "zip", "text/utf-8", "text/ascii");
    public static final Formats TEST_FORMATS_2 = new Formats("image.jpg", "epub.zip", "rdf/xml", "html/utf-8", "mobipocket/ebook", "zip2", "text-plain/utf-8", "text-plain/ascii");

    public static final Book TEST_BOOK_1 = new Book(1, "Alice in Wonderland", TEST_AUTHORS_1, TEST_SUBJECTS_1, TEST_FORMATS_1, TIMESTAMP_1);
    public static final Book TEST_BOOK_2 = new Book(2, "Alice in Wonderland", TEST_AUTHORS_2, TEST_SUBJECTS_2, TEST_FORMATS_2, TIMESTAMP_2);

    public static final List<Book> TEST_BOOKS_LIST = Collections.unmodifiableList(
            new ArrayList<Book>() {{
                add(TEST_BOOK_1);
                add(TEST_BOOK_2);
            }}
    );
}
