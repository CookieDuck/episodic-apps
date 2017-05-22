package com.example.episodicevents.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(value = {"type", "data"}, allowGetters = true)
public class RewindEvent extends Event {
    private final int startOffset;
    private final int endOffset;
    private final double speed;

    @JsonCreator
    public RewindEvent(@JsonProperty(value = "userId") Long userId,
                            @JsonProperty(value = "showId") Long showId,
                            @JsonProperty(value = "episodeId") Long episodeId,
                            @JsonProperty(value = "createdAt") LocalDateTime createdAt,
                            @JsonProperty(value = "data") Map<String, Object> data) {
        super(userId, showId, episodeId, createdAt);
        this.startOffset = data != null ? (int) data.get("startOffset") : -1;
        this.endOffset = data != null ? (int) data.get("endOffset") : -1;
        this.speed = data != null ? (double) data.get("speed") : -1;
    }

    @Override
    public String getType() {
        return "rewind";
    }

    @JsonGetter("data")
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put("startOffset", startOffset);
        data.put("endOffset", endOffset);
        data.put("speed", speed);
        return data;
    }
}
