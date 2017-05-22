package com.example.episodicshows.shows;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ShowRepository extends CrudRepository <Show, Long> {
    List<Show> findAll();
    List<Show> findByIdIn(Collection<Long> ids);
}
