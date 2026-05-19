package com.example.frontend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MovieDetailsDto {
    private Integer filmId;
    private String title;
    private String description;
    private String releaseYear;
    private String language;
    private String rating;
    private Integer length;
    private Integer rentalDuration;
    private Double rentalRate;
    private Double replacementCost;
    private String specialFeatures;
    private List<ActorSummary> actors;
    private List<String> categories;

    @Data
    public static class ActorSummary {
        private Integer actorId;
        private String name;
    }
}
