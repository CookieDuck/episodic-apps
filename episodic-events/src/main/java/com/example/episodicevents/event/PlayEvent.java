package com.example.episodicevents.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@JsonIgnoreProperties(value = {"type", "data"}, allowGetters = true)
public class PlayEvent extends Event {
    private final int offset;

    @JsonCreator
    public PlayEvent(@JsonProperty(value = "userId") Long userId,
                     @JsonProperty(value = "showId") Long showId,
                     @JsonProperty(value = "episodeId") Long episodeId,
                     @JsonProperty(value = "createdAt") LocalDateTime createdAt,
                     @JsonProperty(value = "data") Map<String, Object> data) {
        super(userId, showId, episodeId, createdAt);
        this.offset = data != null ? (int) data.get("offset") : -1;
    }

    @Override
    public String getType() {
        return "play";
    }

    @JsonGetter("data")
    public Map<String, Object> getData() {
        return Collections.singletonMap("offset", offset);
    }
}
