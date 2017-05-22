package com.example.episodicshows.viewings;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "viewings")
@NoArgsConstructor
@Data
public class Viewing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private Long showId;
    private Long episodeId;
    private LocalDateTime updatedAt;
    private int timecode;

    public Viewing(Long userId, Long showId, Long episodeId, LocalDateTime updatedAt, int timecode) {
        this.userId = userId;
        this.showId = showId;
        this.episodeId = episodeId;
        this.updatedAt = updatedAt;
        this.timecode = timecode;
    }
}
