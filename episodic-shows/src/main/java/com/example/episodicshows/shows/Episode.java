package com.example.episodicshows.shows;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "episodes")
@Data
@NoArgsConstructor
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long showId;

    private Integer seasonNumber;

    private Integer episodeNumber;

    public Episode(Integer seasonNumber, Integer episodeNumber) {
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }
}
