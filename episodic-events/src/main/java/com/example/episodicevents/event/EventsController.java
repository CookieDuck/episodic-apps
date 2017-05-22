package com.example.episodicevents.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventsController {
    private final EventRepository repo;

    public EventsController(EventRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/")
    public void createEvent(@RequestBody Event event) {
        repo.save(event);
    }

    @GetMapping("/recent")
    public List<Event> getRecent() {
        int page = 0;
        int size = 20;
        Pageable query = new PageRequest(page, size);
        return repo.findAll(query).getContent();
    }
}
