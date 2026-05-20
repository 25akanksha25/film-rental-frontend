package com.example.frontend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentRentalDto {
    private Integer rentalId;
    private String movieTitle;
    private String customerName;
    private LocalDateTime rentalDate;
    private boolean returned;
}
