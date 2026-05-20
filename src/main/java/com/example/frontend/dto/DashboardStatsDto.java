package com.example.frontend.dto;

import lombok.Data;

@Data
public class DashboardStatsDto {
    private Long totalCustomers;
    private Long totalMovies;
    private Long activeRentals;
    private Double totalRevenue;
}
