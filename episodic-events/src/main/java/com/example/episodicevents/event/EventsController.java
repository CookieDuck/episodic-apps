package com.example.episodicevents.event;

import com.example.episodicevents.publisher.ProgressMessage;
import com.example.episodicevents.publisher.ProgressService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventsController {
    private final EventRepository repo;
    private final ProgressService progressService;

    public EventsController(EventRepository repo, ProgressService progressService) {
        this.repo = repo;
        this.progressService = progressService;
    }

    @PostMapping("/")
    public void createEvent(@RequestBody Event event) {
        repo.save(event);

        if (event.getType().equals("progress")) {
            publish(event);
        }
    }

    @GetMapping("/recent")
    public List<Event> getRecent(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize
    ) {
        Pageable query = new PageRequest(page, pageSize, Sort.Direction.DESC, "createdAt");
        return repo.findAll(query).getContent();
    }

    private void publish(Event event) {
        ProgressMessage progressMessage = new ProgressMessage(
                event.getUserId(),
                event.getEpisodeId(),
                event.getCreatedAt(),
                (int) event.getData().getOrDefault("offset", 0)
        );
        progressService.sendMessage(progressMessage);
    }
}
