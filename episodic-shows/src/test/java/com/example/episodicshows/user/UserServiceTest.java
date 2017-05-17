package com.example.episodicshows.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository repo;

    private UserService service;

    @Before
    public void init() {
        service = new UserService(repo);
    }

    @Test
    public void testServiceCreatesUser() {
        String email = "blah@blah.com";
        when(repo.save(any(User.class))).thenReturn(new User(email));

        User created = service.createUser(new User(email));

        assertEquals(email, created.getEmail());
        verify(repo).save(any(User.class));
        verifyNoMoreInteractions(repo);
    }
}
