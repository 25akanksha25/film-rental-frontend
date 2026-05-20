package com.example.frontend.service;

import com.example.frontend.dto.ActorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Service
public class ActorService {

    private static final Logger log = LoggerFactory.getLogger(ActorService.class);

    private final RestTemplate restTemplate;
    private final String backendUrl;

    public ActorService(RestTemplate restTemplate, @Value("${api.backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.backendUrl = backendUrl;
    }

    private HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllActors(String token, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/actors")
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch actors: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> searchActors(String token, String name, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/actors/search")
                .queryParam("name", name)
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to search actors: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public ActorResponseDto getActorById(String token, Integer id) {
        String url = backendUrl + "/actors/" + id;
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<ActorResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, ActorResponseDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch actor: {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getActorMovies(String token, Integer actorId, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/actors/" + actorId + "/movies")
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch actor's movies: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}
