package com.example.episodicshows.shows;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {
    public static EpisodeModel map(Episode entity) {
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
}
