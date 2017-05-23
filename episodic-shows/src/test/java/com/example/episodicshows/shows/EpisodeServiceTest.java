package com.example.episodicshows.shows;

import com.example.episodicshows.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class EpisodeServiceTest {
    @Mock
    private EpisodeRepository repo;

    @Mock
    private ShowService showService;

    private EpisodeService service;

    @Before
    public void init() {
        service = new EpisodeService(repo, showService);
    }

    @Test
    public void testServiceCanCreateAnEpisodeForAnExistingShow() {
        Long showId = 42L;
        int seasonNumber = 5;
        int episodeNumber = 3;
        Show targetShow = new Show("Make me an episode");
        targetShow.setId(showId);
        Episode episode = new Episode(seasonNumber, episodeNumber);
        when(showService.getShow(anyLong())).thenReturn(targetShow);
        when(repo.save(any(Episode.class))).thenReturn(episode);

        Episode created = service.createEpisode(showId, episode);

        assertEquals((long) showId, (long) created.getShowId());
        assertEquals(seasonNumber, (int) created.getSeasonNumber());
        assertEquals(episodeNumber, (int) created.getEpisodeNumber());
        verify(showService).getShow(anyLong());
        verifyNoMoreInteractions(showService);
        verify(repo).save(any(Episode.class));
        verifyNoMoreInteractions(repo);
    }

    @Test
    public void testServiceCannotCreateAnEpisodeForMissingShow() {
        int seasonNumber = 5;
        int episodeNumber = 3;
        Episode episode = new Episode(seasonNumber, episodeNumber);
        when(showService.getShow(anyLong())).thenReturn(null);

        Episode created = service.createEpisode(1L, episode);

        assertNull(created);
        verify(showService).getShow(anyLong());
        verifyNoMoreInteractions(showService);
        verifyZeroInteractions(repo);
    }

    @Test
    public void testServiceReturnsEmptyForMissingShow() {
        when(repo.findAllByShowId(anyLong())).thenReturn(null);

        List<EpisodeModel> episodes = service.getEpisodesForShow(404L);

        assertTrue(episodes.isEmpty());
        verify(repo).findAllByShowId(anyLong());
        verifyNoMoreInteractions(repo);
        verifyZeroInteractions(showService);
    }

    @Test
    public void testServiceReturnsEpisodesForShow() {
        Episode episode1 = new Episode(1, 2);
        episode1.setId(1L);
        Episode episode2 = new Episode(3, 4);
        episode2.setId(2L);
        when(repo.findAllByShowId(anyLong())).thenReturn(asList(episode1, episode2));

        List<EpisodeModel> episodes = service.getEpisodesForShow(1L);
        List<Integer> seasons = episodes.stream().map(EpisodeModel::getSeasonNumber).collect(Collectors.toList());
        List<Integer> episodeNumbers = episodes.stream().map(EpisodeModel::getEpisodeNumber).collect(Collectors.toList());

        assertEquals(2, episodes.size());
        assertTrue(seasons.containsAll(asList(1, 3)));
        assertTrue(episodeNumbers.containsAll(asList(2, 4)));
        verify(repo).findAllByShowId(anyLong());
        verifyNoMoreInteractions(repo);
        verifyZeroInteractions(showService);
    }
}
