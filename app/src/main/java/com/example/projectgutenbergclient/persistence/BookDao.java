package com.example.projectgutenbergclient.persistence;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projectgutenbergclient.models.Author;
import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.models.Formats;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface BookDao {

    @Insert(onConflict = IGNORE)
    long[] insertBooks(Book... book);

    @Insert(onConflict = REPLACE)
    void insertBook(Book book);

    @Query("UPDATE books SET title = :title, authors = :authors, formats = :formats, subjects = :subjects WHERE book_id = :book_id")
    void updateBook(long book_id, String title, List<Author> authors, Formats formats, ArrayList<String> subjects);

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR subjects LIKE '%' || :query || '%' OR authors LIKE '%' || :query || '%'" +
            "ORDER BY title DESC LIMIT (:pageNumber * 30)")
    LiveData<List<Book>> searchBooks(String query, int pageNumber);

    @Query("SELECT * FROM books WHERE book_id = :book_id")
    LiveData<Book> getBook(long book_id);
}
