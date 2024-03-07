package com.aequalis.utils;

import com.aequalis.dto.BookDto;
import com.aequalis.dto.CartDto;
import com.aequalis.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE= Mappers.getMapper(BookMapper.class);
    BookDto mapToBookDto(Book book);
    Book mapToBook(BookDto bookDto);
    CartDto mapToCartDto(BookDto bookDto);
}
