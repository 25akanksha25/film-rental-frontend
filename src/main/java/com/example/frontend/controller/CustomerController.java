package com.example.frontend.controller;

import com.example.frontend.dto.CityResponseDto;
import com.example.frontend.dto.CustomerRequestDto;
import com.example.frontend.service.CustomerService;
import com.example.frontend.service.LocationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final LocationService locationService;

    public CustomerController(CustomerService customerService, LocationService locationService) {
        this.customerService = customerService;
        this.locationService = locationService;
    }

    @GetMapping
    public String customers(HttpSession session, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String search) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        Map<String, Object> customersPage;
        if (search != null && !search.isBlank()) {
            customersPage = customerService.searchCustomers(token, search, page, size);
        } else {
            customersPage = customerService.getCustomers(token, page, size);
        }

        model.addAttribute("customersPage", customersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("customerRequest", new CustomerRequestDto());
        model.addAttribute("countries", locationService.getCountries());
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        return "customers";
    }

    @PostMapping
    public String createCustomer(HttpSession session,
                                 @ModelAttribute CustomerRequestDto customerRequest,
                                 RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        try {
            customerService.createCustomer(token, customerRequest);
            redirectAttributes.addFlashAttribute("success", "Customer created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/customers";
    }

    // AJAX endpoint for the add-customer modal's cascading dropdown
    @GetMapping("/cities")
    @ResponseBody
    public List<CityResponseDto> citiesByCountry(@RequestParam Integer countryId) {
        return locationService.getCities(countryId);
    }
}
