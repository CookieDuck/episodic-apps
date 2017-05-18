package com.example.episodicshows.shows;

import org.springframework.stereotype.Service;

@Service
public class ShowService {
    private final ShowRepository repo;

    public ShowService(ShowRepository repo) {
        this.repo = repo;
    }

    public Show createShow(Show show) {
        return repo.save(show);
    }

    public Iterable<Show> getShows() {
        return repo.findAll();
    }
}
