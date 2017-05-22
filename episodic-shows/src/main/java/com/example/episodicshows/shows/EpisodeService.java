package com.example.episodicshows.shows;

import com.example.episodicshows.utils.MapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class EpisodeService {
    private final EpisodeRepository repo;
    private final ShowService showService;

    public EpisodeService(EpisodeRepository repo, ShowService showService) {
        this.repo = repo;
        this.showService = showService;
    }

    public List<EpisodeModel> getEpisodesForShow(Long showId) {
        List<Episode> episodes = repo.findAllByShowId(showId);
        return CollectionUtils.isEmpty(episodes) ?
                Collections.emptyList() : MapperUtils.map(episodes);
    }

    public EpisodeModel createEpisodeModel(Long showId, Episode episode) {
        Episode created = createEpisode(showId, episode);
        return created != null ? MapperUtils.map(created) : null;
    }

    public Episode createEpisode(Long showId, Episode episode) {
        Show show = showService.getShow(showId);
        if (show == null) {
            return null;
        }
        episode.setShowId(show.getId());
        return repo.save(episode);
    }

    public List<Episode> getEpisodes(Collection<Long> episodeIds) {
        return repo.findByIdIn(episodeIds);
    }

    public Episode getEpisode(Long episodeId) {
        return repo.findOne(episodeId);
    }
}
