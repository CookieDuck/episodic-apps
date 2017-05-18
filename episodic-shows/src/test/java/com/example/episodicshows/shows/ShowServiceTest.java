package com.example.episodicshows.shows;

import com.example.episodicshows.user.User;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ShowServiceTest {
    @Mock
    private ShowRepository repo;

    private ShowService service;

    @Before
    public void init() {
        service = new ShowService(repo);
    }

    @Test
    public void testServiceCreatesShow() {
        String showName = "Adventure Time";
        when(repo.save(any(Show.class))).thenReturn(new Show(showName));

        Show created = service.createShow(new Show(showName));

        assertEquals(showName, created.getName());
        verify(repo).save(any(Show.class));
        verifyNoMoreInteractions(repo);
    }

    @Test
    public void testServiceReturnsUsers() {
        when(repo.findAll()).thenReturn(asList(new Show("Show uno"), new Show("Show dos")));

        Iterable<Show> shows = service.getShows();

        List<String> names = StreamSupport.stream(shows.spliterator(), false).map(Show::getName).collect(Collectors.toList());

        assertEquals(2, names.size());
        assertTrue(names.containsAll(asList("Show uno", "Show dos")));
        verify(repo).findAll();
        verifyNoMoreInteractions(repo);
    }
}
