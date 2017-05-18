package com.example.episodicshows.shows;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
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
}
