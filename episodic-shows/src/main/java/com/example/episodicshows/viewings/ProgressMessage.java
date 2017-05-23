package com.example.episodicshows.viewings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProgressMessage {
    private final Long userId;
    private final Long episodeId;
    private final LocalDateTime createdAt;
    private final int offset;

    public ProgressMessage(@JsonProperty(value = "userId") Long userId,
                           @JsonProperty(value = "episodeId") Long episodeId,
                           @JsonProperty(value = "createdAt") LocalDateTime createdAt,
                           @JsonProperty(value = "offset") int offset) {
        this.userId = userId;
        this.episodeId = episodeId;
        this.createdAt = createdAt;
        this.offset = offset;
    }
}
