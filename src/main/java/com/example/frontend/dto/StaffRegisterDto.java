package com.example.frontend.dto;

import lombok.Data;

@Data
public class StaffRegisterDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Integer storeId;

    // Address
    private String address;
    private String address2;
    private String district;
    private Integer cityId;
    private String postalCode;
    private String phone;
}
