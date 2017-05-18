package com.example.episodicshows.shows;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public final class EpisodeModel {
    private final Long id;
    private final int seasonNumber;
    private final int episodeNumber;
    private final String title;

    @JsonCreator
    public EpisodeModel(@JsonProperty(value = "id") Long id,
                        @JsonProperty(value = "seasonNumber") int seasonNumber,
                        @JsonProperty(value = "episodeNumber") int episodeNumber,
                        @JsonProperty(value = "title") String title) {
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.title = title;
    }
}
