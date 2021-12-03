package com.example.projectgutenbergclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.projectgutenbergclient.models.Author;
import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.models.Formats;
import com.example.projectgutenbergclient.util.LiveDataTestUtil;
import com.example.projectgutenbergclient.util.TestUtil;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class BookDaoTest extends BookDatabaseTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Test
    public void insertReadDelete() throws Exception {

        Book book = new Book(TestUtil.TEST_BOOK_1);

        // Insert
        getBookDao().insertBook(book).blockingGet();

        // Read
        LiveDataTestUtil<List<Book>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Book> insertedBooks = liveDataTestUtil.getValue(getBookDao().getBooks());
        Book insertedBook = insertedBooks.get(0);

        assertNotNull(insertedBooks);

        assertEquals(book.getId(), insertedBook.getId());
        assertEquals(book.getTitle(), insertedBook.getTitle());
        assertEquals(book.getAuthors(), insertedBook.getAuthors());
        assertEquals(book.getFormats(), insertedBook.getFormats());
        assertEquals(book.getSubjects(), insertedBook.getSubjects());
        assertEquals(book.getTimestamp(), insertedBook.getTimestamp());

        book.setId(insertedBook.getId());
        assertEquals(book, insertedBook);

        // Delete
        getBookDao().deleteBook(book).blockingGet();

        // Confirm the database is empty
        insertedBooks = liveDataTestUtil.getValue(getBookDao().getBooks());
        assertEquals(0, insertedBooks.size());

    }


    @Test
    public void insertReadUpdateReadDelete() throws Exception {
        Book book = new Book(TestUtil.TEST_BOOK_1);

        // Insert
        getBookDao().insertBook(book).blockingGet();

        // Read
        LiveDataTestUtil<List<Book>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Book> insertedBooks = liveDataTestUtil.getValue(getBookDao().getBooks());
        Book insertedBook = insertedBooks.get(0);

        assertNotNull(insertedBooks);

        assertEquals(book.getId(), insertedBook.getId());
        assertEquals(book.getTitle(), insertedBook.getTitle());
        assertEquals(book.getAuthors(), insertedBook.getAuthors());
        assertEquals(book.getFormats(), insertedBook.getFormats());
        assertEquals(book.getSubjects(), insertedBook.getSubjects());
        assertEquals(book.getTimestamp(), insertedBook.getTimestamp());

        book.setId(insertedBook.getId());
        assertEquals(book, insertedBook);

        // Update
        book.setTitle("The Picture of Dorian Gray");
        book.setAuthors(new ArrayList<>(Arrays.asList(
                new Author("Hemingway"),
                new Author("Woolf"),
                new Author("Rowling")
        )));
        book.setSubjects(new ArrayList<>(Arrays.asList(
                "Anthology",
                "Business/Economics",
                "Classic"
        )));
        book.setFormats(new Formats(
                "a",
                "b",
                "c",
                "d",
                "e",
                "f",
                "g",
                "h"
        ));
        book.setTimestamp(
                new GregorianCalendar(1945, 11, 23).getTime().getTime()
        );
        getBookDao().updateBook(book).blockingGet();

        // Read
        insertedBooks = liveDataTestUtil.getValue(getBookDao().getBooks());
        insertedBook = insertedBooks.get(0);

        assertNotNull(insertedBooks);

        assertEquals(book.getId(), insertedBook.getId());
        assertEquals(book.getTitle(), insertedBook.getTitle());
        assertEquals(book.getAuthors(), insertedBook.getAuthors());
        assertEquals(book.getFormats(), insertedBook.getFormats());
        assertEquals(book.getSubjects(), insertedBook.getSubjects());
        assertEquals(book.getTimestamp(), insertedBook.getTimestamp());

        book.setId(insertedBook.getId());
        assertEquals(book, insertedBook);

        // Delete
        getBookDao().deleteBook(book).blockingGet();

        // Confirm the database is empty
        insertedBooks = liveDataTestUtil.getValue(getBookDao().getBooks());
        assertEquals(0, insertedBooks.size());
    }

    @Test(expected = SQLiteConstraintException.class)
    public void insert_nullTitle_throwSQLiteConstraintException() throws Exception {
        final Book book = new Book(TestUtil.TEST_BOOK_1);
        book.setTitle(null);

        // Insert
        getBookDao().insertBook(book).blockingGet();
    }

    @Test(expected = SQLiteConstraintException.class)
    public void updateBook_nullTitle_throwSQLiteConstraintException() throws Exception {
        Book book = new Book(TestUtil.TEST_BOOK_1);

        // Insert
        getBookDao().insertBook(book).blockingGet();

        // Read
        LiveDataTestUtil<List<Book>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Book> insertedBooks = liveDataTestUtil.getValue(getBookDao().getBooks());
        assertNotNull(insertedBooks);
        assertNotEquals(0, insertedBooks.size());

        // Update
        book = new Book(insertedBooks.get(0));
        book.setTitle(null);
        getBookDao().updateBook(book).blockingGet();
    }
}
