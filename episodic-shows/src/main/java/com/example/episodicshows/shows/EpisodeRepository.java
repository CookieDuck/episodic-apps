package com.example.episodicshows.shows;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {
    List<Episode> findAllByShowId(Long showId);
    List<Episode> findAll();
    List<Episode> findByIdIn(Collection<Long> ids);
}
