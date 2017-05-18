package com.example.episodicshows.shows;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowController {
    private final ShowService service;

    public ShowController(ShowService service) {
        this.service = service;
    }

    @PostMapping("/shows")
    public Show createShow(@RequestBody Show show) {
        return service.createShow(show);
    }
}
