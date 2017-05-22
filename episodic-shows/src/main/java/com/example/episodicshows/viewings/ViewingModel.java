package com.example.episodicshows.viewings;

import com.example.episodicshows.shows.EpisodeModel;
import com.example.episodicshows.shows.Show;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Value
public final class ViewingModel {
    private final Show show;
    private final EpisodeModel episode;
    private final LocalDateTime updatedAt;
    private final int timecode;

    @JsonCreator
    public ViewingModel(
           @JsonProperty(value = "show") Show show,
           @JsonProperty(value = "episode") EpisodeModel episode,
           @JsonProperty(value = "updatedAt") LocalDateTime updatedAt,
           @JsonProperty(value = "timecode") int timecode) {
        this.show = show;
        this.episode = episode;
        this.updatedAt = updatedAt;
        this.timecode = timecode;
    }
}
