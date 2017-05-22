package com.example.episodicshows.viewings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public final class ViewingPatch {
    private final Long episodeId;
    private final LocalDateTime updatedAt;
    private final int timecode;

    @JsonCreator
    public ViewingPatch(@JsonProperty(value = "episodeId") Long episodeId,
                        @JsonProperty(value = "updatedAt") LocalDateTime updatedAt,
                        @JsonProperty(value = "timecode") int timecode) {
        this.episodeId = episodeId;
        this.updatedAt = updatedAt;
        this.timecode = timecode;
    }
}
