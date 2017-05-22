package com.example.episodicshows.utils;

import com.example.episodicshows.shows.Episode;
import com.example.episodicshows.shows.EpisodeModel;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.viewings.ViewingModel;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {
    public static EpisodeModel map(Episode entity) {
        if (entity == null || entity.getId() == null) {
            return null;
        }


        String title = String.format("S%d E%d", entity.getSeasonNumber(), entity.getEpisodeNumber());
        return new EpisodeModel(
                entity.getId(),
                entity.getSeasonNumber(),
                entity.getEpisodeNumber(),
                title
        );
    }

    public static List<EpisodeModel> map(Collection<Episode> episodes) {
        return episodes.stream().map(e -> MapperUtils.map(e)).collect(Collectors.toList());
    }

    public static ViewingModel map(Show show, Episode episode, LocalDateTime updatedAt, int timecode) {
        return new ViewingModel(show, map(episode), updatedAt, timecode);
    }
}
