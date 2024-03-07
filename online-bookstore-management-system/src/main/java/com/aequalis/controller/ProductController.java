package com.aequalis.controller;

import com.aequalis.dto.BookDto;
import com.aequalis.dto.CartDto;
import com.aequalis.entity.Book;
import com.aequalis.entity.ShoppingCart;
import com.aequalis.entity.UserInfo;
import com.aequalis.error.ErrorResponse;
import com.aequalis.service.*;
import com.aequalis.dto.AuthRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private UserInfoService service;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BookService bookService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @PostMapping("/new")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }
    @PostMapping("/addNewBook")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addNewBook(@RequestBody BookDto bookDto)
    {
        try {
            logger.info("Add now book");
            String response=bookService.addNewBook(bookDto);
            logger.info("book added success");
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            logger.error("book not saved");
           e.printStackTrace();
           return ResponseEntity.status(404).body("book not saved");
        }
    }

    @PutMapping("/updateBookDetails")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateBookDetails(@RequestBody BookDto bookDto)
    {
        try
        {
            logger.info("book update");
            BookDto bookDto1=bookService.updateBookDetail(bookDto);
            logger.info("book updated");
            return ResponseEntity.ok(bookDto1);
        }
        catch (Exception e)
        {
            logger.error("no value found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "No Value Found"));
        }
    }

    @GetMapping("/viewAllBooks")
    public List<BookDto> viewAllBooks() {
        return bookService.viewAllBooks();
    }

    @GetMapping("/viewBook/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BookDto viewBook(@PathVariable("id")UUID id)
    {
       return bookService.viewBook(id);
    }

    @DeleteMapping("/deleteBook/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteBook(@PathVariable("id") UUID id)
    {
        return bookService.deleteBook(id);
    }

    @PostMapping("/addToCart/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<CartDto> addToCart(@PathVariable("id") UUID id, @RequestBody ShoppingCart shoppingCart)
    {
           return shoppingCartService.addToCart(id,shoppingCart);
    }
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
