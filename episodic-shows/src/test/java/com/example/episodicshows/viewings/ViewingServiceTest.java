package com.example.episodicshows.viewings;

import com.example.episodicshows.shows.EpisodeService;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowService;
import com.example.episodicshows.user.User;
import com.example.episodicshows.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
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
}
