package com.example.episodicshows.user;

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

    @Test
    public void testServiceReturnsUsers() {
        when(repo.findAll()).thenReturn(asList(new User("1@1.com"), new User("2@2.com")));

        Iterable<User> users = service.getUsers();

        List<String> emails = StreamSupport.stream(users.spliterator(), false).map(User::getEmail).collect(Collectors.toList());

        assertEquals(2, emails.size());
        assertTrue(emails.containsAll(asList("1@1.com", "2@2.com")));
        verify(repo).findAll();
        verifyNoMoreInteractions(repo);
    }
}
