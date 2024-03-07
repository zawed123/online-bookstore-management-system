package com.aequalis.service;

import com.aequalis.dto.BookDto;
import com.aequalis.entity.Book;

import java.util.List;
import java.util.UUID;

public interface BookService {

    public String addNewBook(BookDto bookDto);

    public BookDto updateBookDetail(BookDto bookDto);

    public List<BookDto> viewAllBooks();

    public BookDto viewBook(UUID id);

    public String deleteBook(UUID id);
}
