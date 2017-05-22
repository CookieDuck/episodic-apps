package com.example.episodicevents.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    public List<Event> getRecent(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize
    ) {
        Pageable query = new PageRequest(page, pageSize, Sort.Direction.DESC, "createdAt");
        return repo.findAll(query).getContent();
    }
}
