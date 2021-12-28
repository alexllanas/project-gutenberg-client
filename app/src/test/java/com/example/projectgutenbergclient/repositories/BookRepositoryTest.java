package com.example.projectgutenbergclient.repositories;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.projectgutenbergclient.persistence.BookDao;
import com.example.projectgutenbergclient.persistence.BookDatabase;
import com.example.projectgutenbergclient.persistence.ContentPathDao;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatchers;

import java.security.Key;
import java.util.concurrent.Callable;


@RunWith(JUnit4.class)
public class BookRepositoryTest {
    private BookDao bookDao = mock(BookDao.class);
    private ContentPathDao contentPathDao = mock(ContentPathDao.class);
    private BookRepository repo;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        BookDatabase db = mock(BookDatabase.class);
        when(db.getBookDao()).thenReturn(bookDao);
        when(db.getContentPathDao()).thenReturn(contentPathDao);
        when(db.runInTransaction(ArgumentMatchers.<Callable<Object>>any())).thenCallRealMethod();
        repo = new BookRepository(bookDao, contentPathDao);
    }


}
