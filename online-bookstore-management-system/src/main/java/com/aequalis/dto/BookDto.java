package com.aequalis.dto;

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
public class BookDto {
    private UUID id;
    private String title;
    private String author;
    private String description;
    private String price;
    private String publication;
    private Instant date=Instant.now();
    private Integer quantity;
}
