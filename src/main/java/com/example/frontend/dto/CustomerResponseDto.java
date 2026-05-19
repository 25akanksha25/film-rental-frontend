package com.example.frontend.dto;

import lombok.Data;

@Data
public class CustomerResponseDto {
    private Integer customerId;
    private String fullName;
    private String email;
    private Integer storeId;
    private Boolean active;
}
