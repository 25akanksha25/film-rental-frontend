package com.example.frontend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RentalConfirmationDto {
    private Integer rentalId;
    private String movieTitle;
    private String customerName;
    private Integer copyId;
    private BigDecimal amount;
}
