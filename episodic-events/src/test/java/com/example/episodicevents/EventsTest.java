package com.example.episodicevents;

import com.example.episodicevents.event.Event;
import com.example.episodicevents.event.EventRepository;
import com.example.episodicevents.event.PlayEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(secure = false)
public class EventsTest {
    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long DEFAULT_SHOW_ID = 2L;
    private static final Long DEFAULT_EPISODE_ID = 3L;

    @Autowired
    EventRepository repo;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    //TODO deleteAll between each test?

    @Test
    public void createPlayEvent() throws Exception {
        Event playEvent = makePlayEvent(123);
        String postRequest = mapper.writeValueAsString(playEvent);
        mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void getReturnsRecent() throws Exception {
        repo.deleteAll();
        Event playEvent = makePlayEvent(123);
        String postRequest = mapper.writeValueAsString(playEvent);
        mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequest))
                .andExpect(status().isOk());

        mvc.perform(get("/recent").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userId", is(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$[0].showId", is(DEFAULT_SHOW_ID.intValue())))
                .andExpect(jsonPath("$[0].episodeId", is(DEFAULT_EPISODE_ID.intValue())))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].type", is("play")))
                .andExpect(jsonPath("$[0].data.offset", is(123)));
    }

    private PlayEvent makePlayEvent(int offset) {
        Long userId = DEFAULT_USER_ID;
        Long showId = DEFAULT_SHOW_ID;
        Long episodeId = DEFAULT_EPISODE_ID;
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("offset", offset);
        return new PlayEvent(userId, showId, episodeId, createdAt, dataPayload);
    }
}
