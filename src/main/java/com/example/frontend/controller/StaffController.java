package com.example.frontend.controller;

import com.movie_rental_frontend.dto.CityResponseDto;
import com.movie_rental_frontend.dto.StaffRegisterDto;
import com.movie_rental_frontend.dto.StaffResponseDto;
import com.movie_rental_frontend.service.LocationService;
import com.movie_rental_frontend.service.StaffService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;
    private final LocationService locationService;

    public StaffController(StaffService staffService, LocationService locationService) {
        this.staffService = staffService;
        this.locationService = locationService;
    }

    @GetMapping
    public String staffList(HttpSession session, Model model,
                            @RequestParam(required = false) String search,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        Map<String, Object> staffPage = (search != null && !search.isBlank())
                ? staffService.searchStaff(token, search, page, size)
                : staffService.getAllStaff(token, page, size);

        model.addAttribute("staffPage", staffPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("registerDto", new StaffRegisterDto());
        model.addAttribute("countries", locationService.getCountries());
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        return "staff";
    }

    @GetMapping("/{id}")
    public String staffDetails(HttpSession session, Model model, @PathVariable Integer id) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        StaffResponseDto staff = staffService.getStaffById(token, id);

        model.addAttribute("staff", staff);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        return "staff-details";
    }

    @PostMapping
    public String addStaff(HttpSession session,
                           @ModelAttribute StaffRegisterDto registerDto,
                           RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        try {
            staffService.createStaff(token, registerDto);
            redirectAttributes.addFlashAttribute("success", "Staff member added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/staff";
    }

    // AJAX endpoint — the add-staff modal calls this when the user picks a country
    @GetMapping("/cities")
    @ResponseBody
    public List<CityResponseDto> citiesByCountry(@RequestParam Integer countryId) {
        return locationService.getCities(countryId);
    }
}
