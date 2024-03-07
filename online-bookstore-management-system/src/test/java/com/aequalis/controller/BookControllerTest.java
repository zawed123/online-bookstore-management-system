package com.aequalis.controller;

import com.aequalis.dto.AuthRequest;
import com.aequalis.dto.BookDto;
import com.aequalis.dto.CartDto;
import com.aequalis.entity.Book;
import com.aequalis.entity.ShoppingCart;
import com.aequalis.entity.UserInfo;
import com.aequalis.error.ErrorResponse;
import com.aequalis.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookControllerTest {


    @Mock
    private BookService bookService;

    @InjectMocks
    private ProductController productController;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserInfoService service;

    @Test
    public void testAddNewBook_Success() {
        BookDto book=new BookDto();
        book.setAuthor("Balaguruswami");
        book.setTitle("java");
        book.setPrice("459.0");
        book.setDescription("This is java book");
        book.setPublication("bpb");
        book.setQuantity(6);
        String responseMessage = "Book added successfully";
        when(bookService.addNewBook(book)).thenReturn(responseMessage);

        ResponseEntity<?> responseEntity = productController.addNewBook(book);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseMessage, responseEntity.getBody());
    }

    @Test
    public void testAddNewBook_Failure() {
        BookDto book = new BookDto();
        when(bookService.addNewBook(book)).thenThrow(new RuntimeException("Error occurred while adding book"));

        ResponseEntity<?> responseEntity = productController.addNewBook(book);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("book not saved", responseEntity.getBody());
    }

    @Test
    public void testAddNewUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("John Doe");
        userInfo.setEmail("john@example.com");
        userInfo.setPassword("john@123");
        userInfo.setRoles("ROLE_USER");
        when(service.addUser(userInfo)).thenReturn("User added successfully");
        String result = productController.addNewUser(userInfo);
        verify(service).addUser(userInfo);

        assertEquals("User added successfully", result);
    }

    @Test
    public void testUpdateBookDetails_Success() {
        BookDto bookDto = new BookDto();

        when(bookService.updateBookDetail(bookDto)).thenReturn(bookDto);

        ResponseEntity<?> responseEntity = productController.updateBookDetails(bookDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookDto, responseEntity.getBody());
    }

    @Test
    public void testUpdateBookDetails_Failure() {
        BookDto bookDto = new BookDto();

        when(bookService.updateBookDetail(bookDto)).thenThrow(new RuntimeException("Error occurred while updating book"));

        ResponseEntity<?> responseEntity = productController.updateBookDetails(bookDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
        assertEquals("No Value Found", errorResponse.getMessage());
    }

    @Test
    //@WithMockUser(roles = "ADMIN")
    public void testViewBook() {
        UUID bookId = UUID.randomUUID();
        BookDto mockedBook = new BookDto();
        when(bookService.viewBook(bookId)).thenReturn(mockedBook);

        BookDto returnedBook = productController.viewBook(bookId);

        assertEquals(mockedBook, returnedBook);
    }

    @Test
    public void testViewAllBooks() {
        List<BookDto> mockedBooks = new ArrayList<>();

        BookDto book=new BookDto();
        book.setAuthor("Balaguruswami");
        book.setTitle("java");
        book.setPrice("459.0");
        book.setDescription("This is java book");
        book.setPublication("bpb");
        book.setQuantity(6);
        mockedBooks.add(book);
        when(bookService.viewAllBooks()).thenReturn(mockedBooks);

        List<BookDto> returnedBooks = productController.viewAllBooks();

        assertEquals(mockedBooks.size(), returnedBooks.size());
        assertTrue(returnedBooks.containsAll(mockedBooks));
    }

    @Test
    //@WithMockUser(roles = "ADMIN")
    public void testDeleteBook() {
        UUID bookId = UUID.randomUUID();
        String expectedResult = "Book deleted successfully";
        when(bookService.deleteBook(bookId)).thenReturn(expectedResult);

        String result = productController.deleteBook(bookId);

        assertEquals(expectedResult, result);
    }

    @Test
    //@WithMockUser(roles = "ADMIN")
    public void testAddToCart() {
        UUID bookId = UUID.randomUUID();
        ShoppingCart shoppingCart = new ShoppingCart();
        Book book=new Book();
        book.setAuthor("Balaguruswami");
        book.setTitle("java");
        book.setPrice(459.0);
        book.setDescription("This is java book");
        book.setPublication("bpb");
        book.setQuantity(6);
        shoppingCart.setBook(book);
        UserInfo userInfo=new UserInfo();
        userInfo.setName("zawed");
        userInfo.setEmail("zawed123@gmail.com");
        userInfo.setPassword("zawed@123");
        userInfo.setRoles("ROLE_USER");
        shoppingCart.setUserInfo(userInfo);
        List<CartDto> expectedCartDtoList = new ArrayList<>();

        when(shoppingCartService.addToCart(bookId, shoppingCart)).thenReturn(expectedCartDtoList);

        List<CartDto> result = productController.addToCart(bookId, shoppingCart);
        assertEquals(expectedCartDtoList, result);
    }

    @Test
    public void testAuthenticateAndGetToken_Success() {
        String username = "testuser";
        String password = "testpassword";
        String expectedToken = "mockedToken";

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);

        Authentication mockedAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockedAuthentication);
        when(mockedAuthentication.isAuthenticated()).thenReturn(true);

        when(jwtService.generateToken(username)).thenReturn(expectedToken);
        String token = productController.authenticateAndGetToken(authRequest);

        assertEquals(expectedToken, token);
    }

    @Test
    public void testAuthenticateAndGetToken_Failure() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("testpassword");

        when(authenticationManager.authenticate(any())).thenThrow(new UsernameNotFoundException("invalid user request !"));

        assertThrows(UsernameNotFoundException.class, () -> productController.authenticateAndGetToken(authRequest));

        verify(jwtService, never()).generateToken(anyString());
    }
}
