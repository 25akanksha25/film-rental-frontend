package com.example.frontend.service;

import com.example.frontend.dto.LoginRequestDto;
import com.example.frontend.dto.LoginResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final RestTemplate restTemplate;
    private final String backendUrl;

    public AuthService(RestTemplate restTemplate, @Value("${api.backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.backendUrl = backendUrl;
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        String url = backendUrl + "/auth/staff/login";
        try {
            ResponseEntity<LoginResponseDto> response = restTemplate.postForEntity(url, loginRequest, LoginResponseDto.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Login failed: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Invalid username or password");
        }
    }
}
