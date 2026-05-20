package com.example.frontend.dto;

import lombok.Data;

@Data
public class StaffResponseDto {
    private Integer staffId;
    private String fullName;
    private String username;
    private String email;
    private Integer storeId;
    private Boolean active;

    // Filled on the staff-detail endpoint
    private String address;
    private String address2;
    private String district;
    private String city;
    private String country;
    private String postalCode;
    private String phone;
}
