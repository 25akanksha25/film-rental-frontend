package com.example.frontend.controller;

import com.example.frontend.service.RentalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    // ===== Active Rentals List =====
    @GetMapping
    public String rentals(HttpSession session, Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String search,
                          @RequestParam(required = false) Integer customerId) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        Map<String, Object> rentalsPage;
        if (customerId != null) {
            rentalsPage = rentalService.getRentalsByCustomer(token, customerId, page, size);
        } else {
            rentalsPage = rentalService.getActiveRentals(token, search, page, size);
        }

        model.addAttribute("rentalsPage", rentalsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("customerId", customerId);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        return "rentals";
    }

    // ===== Return a Rental =====
    @PostMapping("/return/{rentalId}")
    public String returnRental(HttpSession session,
                               @PathVariable Integer rentalId,
                               RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        try {
            rentalService.returnRental(token, rentalId);
            redirectAttributes.addFlashAttribute("success", "Movie returned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rentals";
    }
}
