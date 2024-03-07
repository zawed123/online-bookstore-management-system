package com.aequalis.dto;

import com.aequalis.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private UUID id;
    private String title;
    private String author;
    private String description;
    private String price;
    private String publication;
    private Integer quantity;
    private Instant date;
}
