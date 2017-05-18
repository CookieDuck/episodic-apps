package com.example.episodicshows.shows;

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
}
