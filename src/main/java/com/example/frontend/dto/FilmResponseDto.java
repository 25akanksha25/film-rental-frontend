package com.example.frontend.dto;

import lombok.Data;

@Data
public class FilmResponseDto {
    private Integer filmId;
    private String title;
    private String description;
    private String releaseYear;
    private String language;
    private Double rentalRate;
    private String rating;
    private Integer length;
}
