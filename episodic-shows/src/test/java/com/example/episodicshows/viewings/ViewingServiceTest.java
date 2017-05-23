package com.example.episodicshows.viewings;

import com.example.episodicshows.shows.Episode;
import com.example.episodicshows.shows.EpisodeService;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowService;
import com.example.episodicshows.user.User;
import com.example.episodicshows.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ViewingServiceTest {
    @Mock
    ViewingRepository viewingRepo;

    @Mock
    UserService userService;

    @Mock
    ShowService showService;

    @Mock
    EpisodeService episodeService;

    private ViewingService service;

    @Before
    public void init() {
        service = new ViewingService(viewingRepo, userService, showService, episodeService);
    }

    @Test
    public void testViewingServiceHandlesNullUserAsEmptyList() {
        when(userService.getUser(anyLong())).thenReturn(null);

        List<ViewingModel> recentViewings = service.getRecentlyWatched(1L);

        assertTrue(recentViewings.isEmpty());
        verify(userService).getUser(anyLong());
        verifyNoMoreInteractions(userService);
        verifyZeroInteractions(viewingRepo);
        verifyZeroInteractions(showService);
        verifyZeroInteractions(episodeService);
    }

    @Test
    public void testViewingServiceHandlesUserWithNoViewingsAsEmptyList() {
        when(userService.getUser(anyLong())).thenReturn(new User("Hey buddy"));
        when(viewingRepo.findAllByUserId(anyLong())).thenReturn(Collections.emptyList());

        List<ViewingModel> recentViewings = service.getRecentlyWatched(1L);

        assertTrue(recentViewings.isEmpty());
        verify(userService).getUser(anyLong());
        verifyNoMoreInteractions(userService);
        verify(viewingRepo).findAllByUserId(anyLong());
        verifyNoMoreInteractions(viewingRepo);
        verifyZeroInteractions(showService);
        verifyZeroInteractions(episodeService);
    }

    @Test
    public void testViewingServiceInteractsWithOtherServicesWhenUserExistsAndHasAtLeastOneViewing() {
        when(userService.getUser(anyLong())).thenReturn(new User("Hey buddy"));
        when(viewingRepo.findAllByUserId(anyLong())).thenReturn(asList(new Viewing()));
        when(showService.getShow(anyLong())).thenReturn(null);
        when(episodeService.getEpisodesForShow(anyLong())).thenReturn(null);

        service.getRecentlyWatched(1L);

        verify(userService).getUser(anyLong());
        verifyNoMoreInteractions(userService);
        verify(viewingRepo).findAllByUserId(anyLong());
        verifyNoMoreInteractions(viewingRepo);
        verify(showService).getShows(anyObject());
        verify(episodeService).getEpisodes(anyObject());
    }

    @Test
    public void testViewingServiceDoesNothingWhenProgressMessageUserDoesNotExist() {
        when(userService.getUser(anyLong())).thenReturn(null);

        service.updateProgress(new ProgressMessage(1L, 2L, LocalDateTime.now(), 1));

        verifyZeroInteractions(episodeService);
        verifyZeroInteractions(viewingRepo);
    }

    @Test
    public void testViewingServiceDoesNothingWhenProgressMessageEpisodeDoesNotExist() {
        when(userService.getUser(anyLong())).thenReturn(new User());
        when(episodeService.getEpisode(anyLong())).thenReturn(null);

        service.updateProgress(new ProgressMessage(1L, 2L, LocalDateTime.now(), 1));

        verifyZeroInteractions(viewingRepo);
    }

    @Test
    public void testViewingServiceMapsProgressMessageMapsToDatabaseCorrectly() {
        when(userService.getUser(anyLong())).thenReturn(new User());
        Episode episode = new Episode();
        episode.setShowId(3L);
        when(episodeService.getEpisode(anyLong())).thenReturn(episode);
        ArgumentCaptor<Viewing> requestCaptor = ArgumentCaptor.forClass(Viewing.class);
        when(viewingRepo.save(requestCaptor.capture())).thenReturn(null);

        final LocalDateTime expectedUpdatedAt = LocalDateTime.now();
        final int expectedTimecode = 12345;
        service.updateProgress(new ProgressMessage(1L, 2L, expectedUpdatedAt, expectedTimecode));

        assertEquals(expectedUpdatedAt, requestCaptor.getValue().getUpdatedAt());
        assertEquals(expectedTimecode, requestCaptor.getValue().getTimecode());
    }
}
