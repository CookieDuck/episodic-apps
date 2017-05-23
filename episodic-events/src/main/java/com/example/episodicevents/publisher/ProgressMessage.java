package com.example.episodicevents.publisher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProgressMessage {
    private final Long userId;
    private final Long episodeId;
    private final LocalDateTime createdAt;
    private final int offset;

    @JsonCreator
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
