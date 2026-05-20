package com.example.frontend.service;

import com.example.frontend.dto.DashboardStatsDto;
import com.example.frontend.dto.RecentRentalDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    private final RestTemplate restTemplate;
    private final String backendUrl;

    public DashboardService(RestTemplate restTemplate, @Value("${api.backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.backendUrl = backendUrl;
    }

    private HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public DashboardStatsDto getStats(String token) {
        String url = backendUrl + "/dashboard/stats";
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<DashboardStatsDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, DashboardStatsDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch dashboard stats: {}", e.getMessage());
            return new DashboardStatsDto();
        }
    }

    public List<RecentRentalDto> getRecentRentals(String token) {
        String url = backendUrl + "/dashboard/recent-rentals";
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<List<RecentRentalDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<RecentRentalDto>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch recent rentals: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
