package com.aequalis.utils;

import com.aequalis.dto.BookDto;
import com.aequalis.dto.CartDto;
import com.aequalis.entity.Book;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-06T22:07:42+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Private Build)"
)
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto mapToBookDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto.BookDtoBuilder bookDto = BookDto.builder();

        bookDto.id( book.getId() );
        bookDto.title( book.getTitle() );
        bookDto.author( book.getAuthor() );
        bookDto.description( book.getDescription() );
        if ( book.getPrice() != null ) {
            bookDto.price( String.valueOf( book.getPrice() ) );
        }
        bookDto.publication( book.getPublication() );
        bookDto.date( book.getDate() );
        bookDto.quantity( book.getQuantity() );

        return bookDto.build();
    }

    @Override
    public Book mapToBook(BookDto bookDto) {
        if ( bookDto == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.id( bookDto.getId() );
        book.title( bookDto.getTitle() );
        book.author( bookDto.getAuthor() );
        book.description( bookDto.getDescription() );
        if ( bookDto.getPrice() != null ) {
            book.price( Double.parseDouble( bookDto.getPrice() ) );
        }
        book.publication( bookDto.getPublication() );
        book.date( bookDto.getDate() );
        book.quantity( bookDto.getQuantity() );

        return book.build();
    }

    @Override
    public CartDto mapToCartDto(BookDto bookDto) {
        if ( bookDto == null ) {
            return null;
        }

        CartDto.CartDtoBuilder cartDto = CartDto.builder();

        cartDto.id( bookDto.getId() );
        cartDto.title( bookDto.getTitle() );
        cartDto.author( bookDto.getAuthor() );
        cartDto.description( bookDto.getDescription() );
        cartDto.price( bookDto.getPrice() );
        cartDto.publication( bookDto.getPublication() );
        cartDto.quantity( bookDto.getQuantity() );
        cartDto.date( bookDto.getDate() );

        return cartDto.build();
    }
}
