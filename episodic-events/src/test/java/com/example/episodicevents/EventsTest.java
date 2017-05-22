package com.example.episodicevents;

import com.example.episodicevents.event.Event;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(secure = false)
public class EventsTest {
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

    private PlayEvent makePlayEvent(int offset) {
        Long userId = 1L;
        Long showId = 2L;
        Long episodeId = 3L;
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("offset", offset);
        return new PlayEvent(userId, showId, episodeId, createdAt, dataPayload);
    }
}
