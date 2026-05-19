package com.example.frontend.dto;

import lombok.Data;

@Data
public class InventoryDto {
    private Integer filmId;
    private String movieTitle;
    private Integer totalCopies;
    private Integer rentedCopies;
    private Integer availableCopies;
}

