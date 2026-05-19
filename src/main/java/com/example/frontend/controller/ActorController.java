package com.example.frontend.controller;

import com.example.frontend.dto.ActorResponseDto;
import com.example.frontend.service.ActorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public String actors(HttpSession session, Model model,
                         @RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        Map<String, Object> actorsPage;
        if (search != null && !search.isBlank()) {
            actorsPage = actorService.searchActors(token, search, page, size);
        } else {
            actorsPage = actorService.getAllActors(token, page, size);
        }

        model.addAttribute("actorsPage", actorsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        return "actors";
    }

    @GetMapping("/{id}/movies")
    public String actorMovies(HttpSession session, Model model,
                              @PathVariable Integer id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) Integer fromMovie) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        ActorResponseDto actor = actorService.getActorById(token, id);
        Map<String, Object> moviesPage = actorService.getActorMovies(token, id, page, size);

        model.addAttribute("actor", actor);
        model.addAttribute("actorId", id);
        model.addAttribute("moviesPage", moviesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("fromMovie", fromMovie);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        return "actor-movies";
    }
}
