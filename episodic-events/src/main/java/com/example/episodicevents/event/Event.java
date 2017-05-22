package com.example.episodicevents.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayEvent.class, name = "play")
})
public abstract class Event {
    @Id
    private Long id;

    private final Long userId;
    private final Long showId;
    private final Long episodeId;
    private final LocalDateTime createdAt;

    public Event(Long userId, Long showId, Long episodeId, LocalDateTime createdAt) {
        this.userId = userId;
        this.showId = showId;
        this.episodeId = episodeId;
        this.createdAt = createdAt;
    }

    @JsonGetter
    public abstract String getType();

    @JsonGetter
    public abstract Map<String, Object> getData();
}
