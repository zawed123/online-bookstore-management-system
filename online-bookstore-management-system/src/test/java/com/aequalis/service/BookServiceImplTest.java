package com.aequalis.service;

import com.aequalis.dto.BookDto;
import com.aequalis.entity.Book;
import com.aequalis.repository.BookRepository;
import com.aequalis.service.impl.BookServiceImpl;
import com.aequalis.utils.BookMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookMapper bookMapper;

//    @Test
//    public void testAddNewBook_Success() {
//        BookDto book=new BookDto();
//        book.setAuthor("Balaguruswami");
//        book.setTitle("java");
//        book.setPrice("459.0");
//        book.setDescription("This is java book");
//        book.setPublication("bpb");
//        book.setQuantity(6);
//
//        when(bookRepository.save(book)).thenReturn(book);
//        String response = bookService.addNewBook(book);
//        assertEquals("Book added successfully", response);
//    }

    @Test
    public void testUpdateBookDetail_ThrowsNoSuchElementException() {
        BookDto bookDto = new BookDto();
        bookDto.setId(UUID.randomUUID());
        when(bookRepository.findById(bookDto.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookService.updateBookDetail(bookDto));


        verify(bookRepository, times(1)).findById(bookDto.getId());
        verify(bookRepository, never()).save(any(Book.class));
    }


    @Test
    void testViewAllBooks_Empty() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(NoSuchElementException.class, () -> {
            bookService.viewAllBooks();
        });
    }


    @Test
    void testViewBook_NonExistingBook() {
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            bookService.viewBook(bookId);
        });
    }

    @Test
    void testDeleteBook_ExistingBook() {
        UUID bookId = UUID.randomUUID();
        when(bookRepository.existsById(bookId)).thenReturn(true);

        String result = bookService.deleteBook(bookId);
        assertNotNull(result);
        assertEquals(bookId + " Book delete successfully", result);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

}
