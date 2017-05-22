package com.example.episodicshows.utils;

import com.example.episodicshows.shows.Episode;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.viewings.ViewingModel;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;

public class MapperUtilsTest {
    @Test
    public void testViewingModelMapperHandlesNullShow() {
        Show show = null;
        Episode episode = new Episode();
        LocalDateTime updatedAt = LocalDateTime.now();
        int timecode = 0;

        ViewingModel model = MapperUtils.map(show, episode, updatedAt, timecode);

        assertNotNull(model);
    }

    @Test
    public void testViewingModelMapperHandlesNullEpisode() {
        Show show = new Show();
        Episode episode = null;
        LocalDateTime updatedAt = LocalDateTime.now();
        int timecode = 0;

        ViewingModel model = MapperUtils.map(show, episode, updatedAt, timecode);

        assertNotNull(model);
    }

    @Test
    public void testViewingModelMapperHandlesNullTimestamp() {
        Show show = new Show();
        Episode episode = new Episode();
        LocalDateTime updatedAt = null;
        int timecode = 0;

        ViewingModel model = MapperUtils.map(show, episode, updatedAt, timecode);

        assertNotNull(model);
    }

    @Test
    public void testViewingModelMapperWorks() {
        Long showId = 12L;
        Show show = new Show("email@address.com");
        show.setId(showId);
        Integer seasonNumber = 34;
        Integer episodeNumber = 56;
        Long episodeId = 78L;
        Episode episode = new Episode(seasonNumber, episodeNumber);
        episode.setId(episodeId);
        LocalDateTime updatedAt = LocalDateTime.now();
        int timecode = 0;

        ViewingModel model = MapperUtils.map(show, episode, updatedAt, timecode);

        assertNotNull(model);
    }
}
