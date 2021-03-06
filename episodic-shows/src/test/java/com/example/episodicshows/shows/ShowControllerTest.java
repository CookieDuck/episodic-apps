package com.example.episodicshows.shows;

import com.example.episodicshows.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(secure = false)
public class ShowControllerTest extends BaseTest {
    @Autowired
    ShowRepository showRepository;

    @Autowired
    EpisodeRepository episodeRepository;

    @Autowired
    MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    @Rollback
    public void testCreateShow() throws Exception {
        String expectedName = "Adventure Time";
        final long initialCount = showRepository.count();

        Map<String, Object> map = new HashMap<>();
        map.put("name", expectedName);
        String requestPayload = mapper.writeValueAsString(map);

        mvc.perform(post("/shows")
                .content(requestPayload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is(expectedName)));
        assertEquals(initialCount + 1, showRepository.count());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetShows() throws Exception {
        showRepository.save(asList(new Show("South Park"), new Show("The Simpsons")));

        mvc.perform(get("/shows")
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$..id").exists())
                .andExpect(jsonPath("$..name", containsInAnyOrder("South Park", "The Simpsons")));
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateEpisode() throws Exception {
        Show show = showRepository.save(new Show("The IT Crowd"));
        final long initialCount = episodeRepository.count();

        Map<String, Object> map = new HashMap<>();
        map.put("seasonNumber", 4);
        map.put("episodeNumber", 2);
        String requestPayload = mapper.writeValueAsString(map);


        mvc.perform(post("/shows/" + show.getId().intValue() + "/episodes")
                .content(requestPayload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.seasonNumber", is(4)))
                .andExpect(jsonPath("$.episodeNumber", is(2)))
                .andExpect(jsonPath("$.title", is("S4 E2")));
        assertEquals(initialCount + 1, episodeRepository.count());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateEpisodeFailsOnMissingShow() throws Exception {
        assertNull("Should not have ANY data", showRepository.findOne(999L));
        final long initialCount = episodeRepository.count();

        Map<String, Object> map = new HashMap<>();
        map.put("seasonNumber", 4);
        map.put("episodeNumber", 2);
        String requestPayload = mapper.writeValueAsString(map);


        mvc.perform(post("/shows/" + 999 + "/episodes")
                .content(requestPayload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest());
        assertEquals(initialCount, episodeRepository.count());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetEpisodesForExistingShow() throws Exception {
        Show show = showRepository.save(new Show("The Simpsons"));
        Episode s1e1 = new Episode(1, 1);
        s1e1.setShowId(show.getId());
        Episode s1e2 = new Episode(1, 2);
        s1e2.setShowId(show.getId());
        episodeRepository.save(s1e1);
        episodeRepository.save(s1e2);

        mvc.perform(get("/shows/" + show.getId() + "/episodes")
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$..id").exists())
                .andExpect(jsonPath("$..seasonNumber", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$..episodeNumber", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$..title", containsInAnyOrder("S1 E1", "S1 E2")));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetEpisodesForMissingShowReturnsEmptyList() throws Exception {
        mvc.perform(get("/shows/999/episodes")
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
