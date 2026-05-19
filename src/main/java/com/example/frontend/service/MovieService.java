package com.example.frontend.service;

import com.example.frontend.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.frontend.dto.MovieDetailsDto;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private final RestTemplate restTemplate;
    private final String backendUrl;

    public MovieService(RestTemplate restTemplate, @Value("${api.backend.url}") String backendUrl) {
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
    public Map<String, Object> getMovies(String token, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/movies")
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch movies: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> searchMovies(String token, String title, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/movies/search")
                .queryParam("title", title)
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to search movies: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> searchByActor(String token, String name, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/movies/actor")
                .queryParam("name", name)
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to search by actor: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> searchByCategory(String token, String name, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/movies/category")
                .queryParam("name", name)
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to search by category: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public MovieDetailsDto getMovieDetails(String token, Integer id) {
        String url = backendUrl + "/movies/" + id + "/details";
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<MovieDetailsDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, MovieDetailsDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch movie details: {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Integer getNextAvailableInventoryId(String token, Integer filmId) {
        String url = backendUrl + "/movies/" + filmId + "/inventory/next-available";
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map body = response.getBody();
            if (body == null) return null;
            Object invId = body.get("inventoryId");
            return invId == null ? null : ((Number) invId).intValue();
        } catch (Exception e) {
            log.error("Failed to fetch next available inventory: {}", e.getMessage());
            return null;
        }
    }

    public InventoryDto getInventory(String token, Integer id) {
        String url = backendUrl + "/movies/" + id + "/inventory";
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<InventoryDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, InventoryDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch inventory: {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getStoreInventory(String token, int page, int size) {
        return getStoreInventory(token, null, page, size);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getStoreInventory(String token, String search, int page, int size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(backendUrl + "/movies/inventory")
                .queryParam("page", page)
                .queryParam("size", size);
        if (search != null && !search.isBlank()) {
            builder.queryParam("search", search);
        }
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch store inventory: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public List<LanguageDto> getAllLanguages(String token) {
        String url = backendUrl + "/movies/languages";
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<List<LanguageDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<LanguageDto>>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch languages: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CategoryDto> getAllCategories(String token) {
        String url = backendUrl + "/movies/categories";
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<CategoryDto>>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch categories: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<ActorResponseDto> getAllActors(String token) {
        // Use the lightweight /actors/basic endpoint — id + name only, no per-actor
        // movie count query. Massively faster for the inventory modal dropdown.
        String url = backendUrl + "/actors/basic";
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<List<ActorResponseDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<ActorResponseDto>>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch actors: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

}
