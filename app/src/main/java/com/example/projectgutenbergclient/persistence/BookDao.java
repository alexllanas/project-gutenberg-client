package com.example.projectgutenbergclient.persistence;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectgutenbergclient.models.Author;
import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.models.Formats;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;


@Dao
public interface BookDao {

    @Insert(onConflict = IGNORE)
    long[] insertBooks(Book... book);

    @Query("UPDATE books SET title = :title, authors = :authors, formats = :formats, subjects = :subjects WHERE id = :book_id")
    void updateBook(long book_id, String title, List<Author> authors, Formats formats, ArrayList<String> subjects);

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR subjects LIKE '%' || :query || '%' OR authors LIKE '%' || :query || '%'" +
            "ORDER BY title DESC LIMIT (:pageNumber * 30)")
    LiveData<List<Book>> searchBooks(String query, int pageNumber);

    @Query("SELECT * FROM books WHERE id = :book_id")
    LiveData<Book> getBook(long book_id);


    @Query("SELECT * FROM books")
    LiveData<List<Book>> getBooks();

    @Insert
    Single<Long> insertBook(Book book) throws Exception;

    @Delete
    Single<Integer> deleteBook(Book book) throws Exception;

    @Update
    Single<Integer> updateBook(Book book) throws Exception;
}
