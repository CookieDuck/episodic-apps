package com.example.episodicevents.event;

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
        @JsonSubTypes.Type(value = PlayEvent.class, name = "play"),
        @JsonSubTypes.Type(value = PauseEvent.class, name = "pause"),
        @JsonSubTypes.Type(value = FastForwardEvent.class, name = "fastForward"),
        @JsonSubTypes.Type(value = ProgressEvent.class, name = "progress"),
        @JsonSubTypes.Type(value = ScrubEvent.class, name = "scrub"),
        @JsonSubTypes.Type(value = RewindEvent.class, name = "rewind")
})
public abstract class Event {
    @Id
    private String id;

    private final Long userId;
    private final Long showId;
    private final Long episodeId;
    private final LocalDateTime createdAt;

    // Required due to Mongo Mapper wanting this sort of field to exist in order to instantiate objects
    private final Map<String, Object> data = null;

    public Event(Long userId, Long showId, Long episodeId, LocalDateTime createdAt) {
        this.userId = userId;
        this.showId = showId;
        this.episodeId = episodeId;
        this.createdAt = createdAt;
    }

    public abstract String getType();
}
