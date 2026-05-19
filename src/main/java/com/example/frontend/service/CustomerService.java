package com.example.frontend.service;

import com.example.frontend.dto.CustomerRequestDto;
import com.example.frontend.dto.CustomerResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final RestTemplate restTemplate;
    private final String backendUrl;

    public CustomerService(RestTemplate restTemplate, @Value("${api.backend.url}") String backendUrl) {
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
    public Map<String, Object> getCustomers(String token, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/customers")
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch customers: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> searchCustomers(String token, String name, int page, int size) {
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/customers/search")
                .queryParam("name", name)
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to search customers: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * Search customers by name and look for an email match in the results.
     * The backend /customers/search endpoint searches by name, so we fetch
     * a large page and filter by email on the frontend side.
     */
    @SuppressWarnings("unchecked")
    public CustomerResponseDto findCustomerByEmail(String token, String email) {
        // The backend search endpoint searches by name. We'll search with a large
        // page size and filter by email. If the backend adds email search later,
        // this can be simplified.
        URI uri = UriComponentsBuilder.fromUriString(backendUrl + "/customers")
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .build().toUri();
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
                for (Map<String, Object> cust : content) {
                    String custEmail = (String) cust.get("email");
                    if (email.equalsIgnoreCase(custEmail)) {
                        CustomerResponseDto dto = new CustomerResponseDto();
                        dto.setCustomerId((Integer) cust.get("customerId"));
                        dto.setFullName((String) cust.get("fullName"));
                        dto.setEmail(custEmail);
                        dto.setStoreId((Integer) cust.get("storeId"));
                        dto.setActive((Boolean) cust.get("active"));
                        return dto;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to find customer by email: {}", e.getMessage());
            return null;
        }
    }

    public CustomerResponseDto getCustomerById(String token, Integer id) {
        String url = backendUrl + "/customers/" + id;
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        try {
            ResponseEntity<CustomerResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, CustomerResponseDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch customer: {}", e.getMessage());
            return null;
        }
    }

    public String createCustomer(String token, CustomerRequestDto customerRequest) {
        String url = backendUrl + "/customers";
        HttpEntity<CustomerRequestDto> entity = new HttpEntity<>(customerRequest, createAuthHeaders(token));
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to create customer: {}", e.getMessage());
            throw new RuntimeException("Failed to create customer: " + e.getMessage());
        }
    }
}
