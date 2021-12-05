package com.example.projectgutenbergclient.repositories;

import com.example.projectgutenbergclient.persistence.BookDao;
import com.example.projectgutenbergclient.persistence.ContentPathDao;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BookRepositoryTest {

    // System under test
    private BookRepository bookRepository;

    @Mock
    private BookDao bookDao;

    @Mock
    private ContentPathDao contentPathDao;

    @BeforeEach
    public void initEach() {
        MockitoAnnotations.initMocks(this);
        bookRepository = new BookRepository(bookDao, contentPathDao);
    }

    /**
     * Insert a list of books.
     * Verify correct method is called
     *
     * Confirm new rows are inserted
     */
    @Test
    void insertBooks_returnRows() throws Exception {
        // Arrange

        // Act

        // Assert

    }

//    @Test
//    void insertBook_returnRow() throws Exception {
//        // Arrange
//        final Long insertedRow = 1L;
//        final Single<Long> returnedData = Single.just(insertedRow);
//        when(bookDao.insertBook(any(Book.class))).thenReturn(returnedData);
//
//        // Act
//        final Resource<Integer> returnedValue = bookRepository.
//    }
}
