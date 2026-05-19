package com.example.frontend.service;

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
public class RentalService {

    private static final Logger log = LoggerFactory.getLogger(RentalService.class);

    private final RestTemplate restTemplate;
    private final String backendUrl;

    public RentalService(RestTemplate restTemplate, @Value("${api.backend.url}") String backendUrl) {
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
    public Map<String, Object> getActiveRentals(String token, String search, int page, int size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(backendUrl + "/rentals/active")
                .queryParam("page", page)
                .queryParam("size", size);
        if (search != null && !search.trim().isEmpty()) {
            builder.queryParam("search", search.trim());
        }
        URI uri = builder.build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch active rentals: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getRentalsByCustomer(String token, Integer customerId, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/rentals/customer/" + customerId)
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch customer rentals: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public String returnRental(String token, Integer rentalId) {
        String url = backendUrl + "/rentals/return/" + rentalId;
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to return rental: {}", e.getMessage());
            throw new RuntimeException("Failed to return rental: " + e.getMessage());
        }
    }


}
