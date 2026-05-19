package com.example.frontend.dto;

import lombok.Data;

@Data
public class RentalResponseDto {
    private Integer rentalId;
    private String movieTitle;
    private String customerName;
    private String rentalDate;
    private String returnDate;
    private Boolean returned;
}
