package com.example.episodicshows.shows;

import org.springframework.stereotype.Service;

@Service
public class ShowService {
    private final ShowRepository showRepo;
    private final EpisodeRepository episodeRepo;

    public ShowService(ShowRepository showRepo, EpisodeRepository episodeRepo) {
        this.showRepo = showRepo;
        this.episodeRepo = episodeRepo;
    }

    public Show createShow(Show show) {
        return showRepo.save(show);
    }

    public Iterable<Show> getShows() {
        return showRepo.findAll();
    }

    public Episode createEpisode(Long showId, Episode episode) {
        Show show = showRepo.findOne(showId);
        if (show == null) {
            return null;
        }
        episode.setShowId(show.getId());
        return episodeRepo.save(episode);
    }
}
