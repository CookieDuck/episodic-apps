package com.example.episodicshows.viewings;

import com.example.episodicshows.shows.Episode;
import com.example.episodicshows.shows.EpisodeRepository;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowRepository;
import com.example.episodicshows.user.User;
import com.example.episodicshows.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(secure = false)
public class ViewingControllerTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ShowRepository showRepository;

    @Autowired
    EpisodeRepository episodeRepository;

    @Autowired
    ViewingRepository viewingRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @Test
    @Transactional
    @Rollback
    public void testGetViewingsReturnsEmptyForMissingUser() throws Exception {
        mvc.perform(get("/users/999/recently-watched"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetViewingsReturnsEmptyForUserWithNoRecentlyWatched() throws Exception {
        User user = userRepository.save(new User("Haven't watched nothin'"));
        mvc.perform(get("/users/" + user.getId().intValue() + "/recently-watched"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetViewingsReturnsRecentlyViewedForUser() throws Exception {
        User user = userRepository.save(new User("not_in_payload_so_who_cares@whatever.edu"));
        Show show1 = showRepository.save(new Show("Show 1"));
        Episode show1Season1Episode1 = new Episode(1, 1);
        show1Season1Episode1.setShowId(show1.getId());
        episodeRepository.save(show1Season1Episode1);
        viewingRepository.save(new Viewing(user.getId(), show1.getId(), show1Season1Episode1.getId(), LocalDateTime.now(), 23));

        mvc.perform(get("/users/" + user.getId().intValue() + "/recently-watched"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].show.id").exists())
                .andExpect(jsonPath("$[0].show.name", is("Show 1")))

                .andExpect(jsonPath("$[0].episode.id").exists())
                .andExpect(jsonPath("$[0].episode.seasonNumber", is(1)))
                .andExpect(jsonPath("$[0].episode.episodeNumber", is(1)))
                .andExpect(jsonPath("$[0].episode.title", is("S1 E1")))

                .andExpect(jsonPath("$[0].updatedAt").exists())
                .andExpect(jsonPath("$[0].timecode", is(23)));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetViewingsReturnsRecentlyViewedForUserSortedByUpdatedAt() throws Exception {
        User user = userRepository.save(new User("not_in_payload_so_who_cares@whatever.edu"));
        Show show1 = showRepository.save(new Show("Show 1"));
        Episode show1Season1Episode1 = new Episode(1, 1);
        Episode show1Season1Episode2 = new Episode(1, 2);
        show1Season1Episode1.setShowId(show1.getId());
        show1Season1Episode2.setShowId(show1.getId());
        episodeRepository.save(show1Season1Episode1);
        episodeRepository.save(show1Season1Episode2);

        viewingRepository.save(new Viewing(user.getId(), show1.getId(), show1Season1Episode1.getId(), LocalDateTime.now(), 23));
        viewingRepository.save(new Viewing(user.getId(), show1.getId(), show1Season1Episode2.getId(), LocalDateTime.now().plusHours(1), 234));

        mvc.perform(get("/users/" + user.getId().intValue() + "/recently-watched"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].episode.title", is("S1 E1")))
                .andExpect(jsonPath("$[1].episode.title", is("S1 E2")));

    }

    @Test
    @Transactional
    @Rollback
    public void testPatchViewingsCreatesIfAbsent() throws Exception {
        User user = userRepository.save(new User("not_in_payload_so_who_cares@whatever.edu"));
        Show show1 = showRepository.save(new Show("Show 1"));
        Episode show1Season1Episode1 = new Episode(1, 1);
        show1Season1Episode1.setShowId(show1.getId());
        Episode savedEpisode = episodeRepository.save(show1Season1Episode1);
        Map<String, Object> patchRequest = new HashMap<>();
        patchRequest.put("episodeId", savedEpisode.getId());
        patchRequest.put("updatedAt", LocalDateTime.now());
        patchRequest.put("timecode", 123);

        assertTrue(viewingRepository.findAllByUserId(user.getId()).isEmpty());
        mvc.perform(patch("/users/" + user.getId().intValue() + "/viewings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk());

        assertFalse(viewingRepository.findAllByUserId(user.getId()).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testPatchViewingsUpdatesIfPresent() throws Exception {
        User user = userRepository.save(new User("not_in_payload_so_who_cares@whatever.edu"));
        Show show1 = showRepository.save(new Show("Show 1"));
        Episode show1Season1Episode1 = new Episode(1, 1);
        show1Season1Episode1.setShowId(show1.getId());
        Episode savedEpisode = episodeRepository.save(show1Season1Episode1);
        Map<String, Object> patchRequest = new HashMap<>();
        patchRequest.put("episodeId", savedEpisode.getId());
        patchRequest.put("updatedAt", LocalDateTime.now());
        patchRequest.put("timecode", 123);

        assertTrue(viewingRepository.findAllByUserId(user.getId()).isEmpty());
        mvc.perform(patch("/users/" + user.getId().intValue() + "/viewings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk());

        assertEquals(1, viewingRepository.findAllByUserId(user.getId()).size());

        patchRequest.put("timecode", 1234);
        mvc.perform(patch("/users/" + user.getId().intValue() + "/viewings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk());

        assertEquals(1, viewingRepository.findAllByUserId(user.getId()).size());
    }
}
