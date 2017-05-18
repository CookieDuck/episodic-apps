package com.example.episodicshows.shows;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

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

    public List<Show> getShows() {
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

    public List<Episode> getEpisodesForShow(Long showId) {
        List<Episode> episodes = episodeRepo.findAllByShowId(showId);
        return CollectionUtils.isEmpty(episodes) ? Collections.emptyList() : episodes;
    }
}
