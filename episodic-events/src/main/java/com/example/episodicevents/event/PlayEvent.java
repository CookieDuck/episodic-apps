package com.example.episodicevents.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@JsonIgnoreProperties(value = "type")
public class PlayEvent extends Event {
    private final int offset;

    @JsonCreator
    public PlayEvent(@JsonProperty(value = "userId") Long userId,
                     @JsonProperty(value = "showId") Long showId,
                     @JsonProperty(value = "episodeId") Long episodeId,
                     @JsonProperty(value = "createdAt") LocalDateTime createdAt,
                     @JsonProperty(value = "data") Map<String, Object> payload) {
        super(userId, showId, episodeId, createdAt);
        this.offset = (int) payload.get("offset");
    }

    @Override
    public String getType() {
        return "play";
    }

    @Override
    public Map<String, Object> getData() {
        return Collections.singletonMap("offset", offset);
    }
}
