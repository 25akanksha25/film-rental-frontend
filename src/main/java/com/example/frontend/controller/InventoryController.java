package com.example.frontend.controller;

import com.example.frontend.dto.MovieCreateRequestDto;
import com.example.frontend.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final MovieService movieService;

    public InventoryController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public String inventory(HttpSession session, Model model,
                            @RequestParam(required = false) String search,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        Map<String, Object> inventoryPage = movieService.getStoreInventory(token, search, page, size);

        model.addAttribute("inventoryPage", inventoryPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("languages", movieService.getAllLanguages(token));
        model.addAttribute("categories", movieService.getAllCategories(token));
        model.addAttribute("actors", movieService.getAllActors(token));
        return "inventory";
    }

    @PostMapping("/add-movie")
    public String addMovie(HttpSession session,
                           @ModelAttribute MovieCreateRequestDto request,
                           RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        boolean success = movieService.createMovie(token, request);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Movie \"" + request.getTitle() + "\" added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add movie. Please try again.");
        }
        return "redirect:/inventory";
    }
}

