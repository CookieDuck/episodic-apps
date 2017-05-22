package com.example.episodicevents.event;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventsController {
    @PostMapping("/")
    public void createEvent(@RequestBody Event event) {
        int j = 0;
        j++;
    }
}
