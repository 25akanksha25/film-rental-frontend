package com.example.frontend.controller;

import com.example.frontend.dto.DashboardStatsDto;
import com.example.frontend.dto.RecentRentalDto;
import com.example.frontend.service.DashboardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        DashboardStatsDto stats = dashboardService.getStats(token);
        List<RecentRentalDto> recentRentals = dashboardService.getRecentRentals(token);

        model.addAttribute("stats", stats);
        model.addAttribute("recentRentals", recentRentals);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        return "dashboard";
    }
}
