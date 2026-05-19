package com.example.frontend.dto;

import lombok.Data;

@Data
public class ActorResponseDto {
    private Integer actorId;
    private String actorName;
    private Integer totalMovies;
}
