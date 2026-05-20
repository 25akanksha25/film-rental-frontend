package com.example.frontend.service;


import com.example.frontend.dto.CountryResponseDto;
import com.example.frontend.dto.CityResponseDto;
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

@Service
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final RestTemplate restTemplate;
    private final String backendUrl;

    public LocationService(RestTemplate restTemplate, @Value("${api.backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.backendUrl = backendUrl;
    }

    private HttpHeaders auth(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<CountryResponseDto> getCountries() {
        String url = backendUrl + "/locations/countries";
        try {
            ResponseEntity<List<CountryResponseDto>> r = restTemplate.exchange(
                    url, HttpMethod.GET, HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<CountryResponseDto>>() {});
            return r.getBody() != null ? r.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch countries: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CityResponseDto> getCities(Integer countryId) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/locations/cities")
                .queryParam("countryId", countryId)
                .build().toUri();
        try {
            ResponseEntity<List<CityResponseDto>> r = restTemplate.exchange(
                    uri, HttpMethod.GET, HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<CityResponseDto>>() {});
            return r.getBody() != null ? r.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch cities for country {}: {}", countryId, e.getMessage());
            return Collections.emptyList();
        }
    }

    // Kept for source-compat with old token-aware callers
    public List<CountryResponseDto> getCountries(String token) { return getCountries(); }
    public List<CityResponseDto> getCities(String token, Integer countryId) { return getCities(countryId); }
}

