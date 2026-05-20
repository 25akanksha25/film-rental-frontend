package com.example.frontend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MovieCreateRequestDto {
    private String title;
    private String description;
    private String releaseYear;
    private Integer languageId;
    private Integer rentalDuration;
    private Double rentalRate;
    private Integer length;
    private Double replacementCost;
    private String rating;
    private String specialFeatures;
    private List<Integer> actorIds;
    private List<NewActorDto> newActors;
    private List<Integer> categoryIds;
    private Integer copies;
}

