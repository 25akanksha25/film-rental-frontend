package com.example.frontend.dto;

import lombok.Data;

@Data
public class RentalRequestDto {

    private Integer inventoryId;
    private Integer customerId;
    private Integer staffId;
}
