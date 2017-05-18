package com.example.episodicshows.shows;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shows")
public class ShowController {
    private final ShowService service;

    public ShowController(ShowService service) {
        this.service = service;
    }

    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return service.createShow(show);
    }

    @GetMapping
    public Iterable<Show> getShows() {
        return service.getShows();
    }
}
