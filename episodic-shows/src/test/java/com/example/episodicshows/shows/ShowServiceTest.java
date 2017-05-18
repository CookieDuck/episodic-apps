package com.example.episodicshows.shows;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ShowServiceTest {
    @Mock
    private ShowRepository showRepo;

    @Mock
    private EpisodeRepository episodeRepo;

    private ShowService service;

    @Before
    public void init() {
        service = new ShowService(showRepo, episodeRepo);
    }

    @Test
    public void testServiceCreatesShow() {
        String showName = "Adventure Time";
        when(showRepo.save(any(Show.class))).thenReturn(new Show(showName));

        Show created = service.createShow(new Show(showName));

        assertEquals(showName, created.getName());
        verify(showRepo).save(any(Show.class));
        verifyNoMoreInteractions(showRepo);
        verifyZeroInteractions(episodeRepo);
    }

    @Test
    public void testServiceReturnsUsers() {
        when(showRepo.findAll()).thenReturn(asList(new Show("Show uno"), new Show("Show dos")));

        List<Show> shows = service.getShows();

        List<String> names = shows.stream().map(Show::getName).collect(Collectors.toList());

        assertEquals(2, names.size());
        assertTrue(names.containsAll(asList("Show uno", "Show dos")));
        verify(showRepo).findAll();
        verifyNoMoreInteractions(showRepo);
        verifyZeroInteractions(episodeRepo);
    }

    @Test
    public void testServiceCanCreateAnEpisodeForAnExistingShow() {
        Long showId = 42L;
        int seasonNumber = 5;
        int episodeNumber = 3;
        Show targetShow = new Show("Make me an episode");
        targetShow.setId(showId);
        Episode episode = new Episode(seasonNumber, episodeNumber);
        when(showRepo.findOne(anyLong())).thenReturn(targetShow);
        when(episodeRepo.save(any(Episode.class))).thenReturn(episode);

        Episode created = service.createEpisode(showId, episode);

        assertEquals((long) showId, (long) created.getShowId());
        assertEquals(seasonNumber, (int) created.getSeasonNumber());
        assertEquals(episodeNumber, (int) created.getEpisodeNumber());
        verify(showRepo).findOne(anyLong());
        verifyNoMoreInteractions(showRepo);
        verify(episodeRepo).save(any(Episode.class));
        verifyNoMoreInteractions(episodeRepo);
    }

    @Test
    public void testServiceCannotCreateAnEpisodeForMissingShow() {
        int seasonNumber = 5;
        int episodeNumber = 3;
        Episode episode = new Episode(seasonNumber, episodeNumber);
        when(showRepo.findOne(anyLong())).thenReturn(null);

        Episode created = service.createEpisode(1L, episode);

        assertNull(created);
        verify(showRepo).findOne(anyLong());
        verifyNoMoreInteractions(showRepo);
        verifyZeroInteractions(episodeRepo);
    }

    @Test
    public void testServiceReturnsEmptyForMissingShow() {
        when(episodeRepo.findAllByShowId(anyLong())).thenReturn(null);

        List<Episode> episodes = service.getEpisodesForShow(404L);

        assertTrue(episodes.isEmpty());
        verify(episodeRepo).findAllByShowId(anyLong());
        verifyNoMoreInteractions(episodeRepo);
        verifyZeroInteractions(showRepo);
    }

    @Test
    public void testServiceReturnsEpisodesForShow() {
        when(episodeRepo.findAllByShowId(anyLong())).thenReturn(asList(new Episode(1, 2), new Episode(3, 4)));

        List<Episode> episodes = service.getEpisodesForShow(1L);
        List<Integer> seasons = episodes.stream().map(Episode::getSeasonNumber).collect(Collectors.toList());
        List<Integer> episodeNumbers = episodes.stream().map(Episode::getEpisodeNumber).collect(Collectors.toList());

        assertEquals(2, episodes.size());
        assertTrue(seasons.containsAll(asList(1, 3)));
        assertTrue(episodeNumbers.containsAll(asList(2, 4)));
        verify(episodeRepo).findAllByShowId(anyLong());
        verifyNoMoreInteractions(episodeRepo);
        verifyZeroInteractions(showRepo);
    }
}
