package com.example.projectgutenbergclient.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.lifecycle.MutableLiveData;

import com.example.projectgutenbergclient.models.Book;
import com.example.projectgutenbergclient.persistence.BookDao;
import com.example.projectgutenbergclient.persistence.ContentPathDao;
import com.example.projectgutenbergclient.util.LiveDataTestUtil;
import com.example.projectgutenbergclient.util.Resource;
import com.example.projectgutenbergclient.util.TestUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;


@ExtendWith(InstantExecutorExtension.class)
public class BookRepositoryTest {

    // System under test
    private BookRepository bookRepository;

    private BookDao bookDao;

    private ContentPathDao contentPathDao;

    @BeforeEach
    public void initEach() {
        bookDao = mock(BookDao.class);
        contentPathDao = mock(ContentPathDao.class);
        bookRepository = new BookRepository(bookDao, contentPathDao);
    }

    @Test
    void getBooks_returnListWithBooks() throws Exception {
        // Arrange
        List<Book> books = TestUtil.TEST_BOOKS_LIST;
        LiveDataTestUtil<List<Book>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Book>> returnedData = new MutableLiveData<>();
        returnedData.setValue(books);
        when(bookDao.getBooks()).thenReturn(returnedData);

        // Act
        List<Book> observedData = liveDataTestUtil.getValue(bookRepository.getBooks());

        // Assert
        assertEquals(books, observedData);
    }

    @Test
    void getBooks_returnEmptyList() throws Exception {
        // Arrange
        List<Book> books = new ArrayList<>();
        LiveDataTestUtil<List<Book>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Book>> returnedData = new MutableLiveData<>();
        returnedData.setValue(books);
        when(bookDao.getBooks()).thenReturn(returnedData);

        // Act
        List<Book> observedData = liveDataTestUtil.getValue(bookRepository.getBooks());

        // Assert
        assertEquals(books, observedData);
    }
}
