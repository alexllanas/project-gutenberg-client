package com.example.projectgutenbergclient;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.projectgutenbergclient.persistence.BookDao;
import com.example.projectgutenbergclient.persistence.BookDatabase;

import org.junit.After;
import org.junit.Before;

public abstract class BookDatabaseTest {

    private BookDatabase bookDatabase;

    public BookDao getBookDao() {
        return bookDatabase.getBookDao();
    }

    @Before
    public void init() {
        bookDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                BookDatabase.class
        ).build();
    }

    @After
    public void finish() {
        bookDatabase.close();;
    }
}
