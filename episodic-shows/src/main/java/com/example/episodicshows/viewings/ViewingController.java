package com.example.episodicshows.viewings;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ViewingController {
    private final ViewingService service;

    public ViewingController(ViewingService service) {
        this.service = service;
    }

    @GetMapping("/users/{id}/recently-watched")
    public List<ViewingModel> getRecentlyWatched(@PathVariable(value = "id") Long userId) {
        return service.getRecentlyWatched(userId);
    }

    @PatchMapping("/users/{id}/viewings")
    public void update(@PathVariable(value = "id") Long userId, @RequestBody ViewingPatch patch) {
        service.update(userId, patch);
    }
}
