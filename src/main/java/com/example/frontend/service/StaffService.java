package com.example.frontend.service;

import com.example.frontend.dto.StaffRegisterDto;
import com.example.frontend.dto.StaffResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class StaffService {

    private static final Logger log = LoggerFactory.getLogger(StaffService.class);

    private final RestTemplate restTemplate;
    private final String backendUrl;

    public StaffService(RestTemplate restTemplate, @Value("${api.backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.backendUrl = backendUrl;
    }

    private HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public Map<String, Object> getAllStaff(String token, int page, int size) {
        URI uri = UriComponentsBuilder
                .fromUriString(backendUrl + "/staff")
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch staff: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public Map<String, Object> searchStaff(String token, String name, int page, int size) {
        URI uri = UriComponentsBuilder
                .fromUriString(backendUrl + "/staff/search")
                .queryParam("name", name)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to search staff: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public String createStaff(String token, StaffRegisterDto dto) {
        String url = backendUrl + "/staff";
        HttpEntity<StaffRegisterDto> entity = new HttpEntity<>(dto, createAuthHeaders(token));
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to create staff: {}", e.getMessage());
            throw new RuntimeException("Failed to create staff: " + e.getMessage());
        }
    }

    public StaffResponseDto getStaffById(String token, Integer id) {
        String url = backendUrl + "/staff/" + id;
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<StaffResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, StaffResponseDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch staff member: {}", e.getMessage());
            return null;
        }
    }

}
