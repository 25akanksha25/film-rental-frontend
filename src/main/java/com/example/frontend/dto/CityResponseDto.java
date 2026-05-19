package com.example.frontend.dto;

import lombok.Data;

@Data
public class CityResponseDto {
    private Integer cityId;
    private String name;
    private Integer countryId;
}
