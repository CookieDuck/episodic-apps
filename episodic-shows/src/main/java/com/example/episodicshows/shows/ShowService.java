package com.example.episodicshows.shows;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ShowService {
    private final ShowRepository showRepo;

    public ShowService(ShowRepository showRepo) {
        this.showRepo = showRepo;
    }

    public Show createShow(Show show) {
        return showRepo.save(show);
    }

    public List<Show> getShows() {
        return showRepo.findAll();
    }

    public List<Show> getShows(Collection<Long> ids) {
        return showRepo.findByIdIn(ids);
    }

    public Show getShow(Long showId) {
        return showRepo.findOne(showId);
    }
}
