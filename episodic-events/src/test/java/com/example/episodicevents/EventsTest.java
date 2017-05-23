package com.example.episodicevents;

import com.example.episodicevents.event.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(secure = false)
public class EventsTest extends BaseTest {
    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long DEFAULT_SHOW_ID = 2L;
    private static final Long DEFAULT_EPISODE_ID = 3L;

    @Autowired
    EventRepository repo;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Before
    public void init() {
        repo.deleteAll();
    }

    @Test
    public void createPlayEvent() throws Exception {
        Event event = makePlayEvent(123);
        doPost(event);

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

    @Test
    public void createPauseEvent() throws Exception {
        Event event = makePauseEvent(123);
        doPost(event);

        mvc.perform(get("/recent").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userId", is(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$[0].showId", is(DEFAULT_SHOW_ID.intValue())))
                .andExpect(jsonPath("$[0].episodeId", is(DEFAULT_EPISODE_ID.intValue())))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].type", is("pause")))
                .andExpect(jsonPath("$[0].data.offset", is(123)));
    }

    @Test
    public void createFastForwardEvent() throws Exception {
        Event event = makeFastForwardEvent(12, 34, 56);
        doPost(event);

        mvc.perform(get("/recent").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userId", is(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$[0].showId", is(DEFAULT_SHOW_ID.intValue())))
                .andExpect(jsonPath("$[0].episodeId", is(DEFAULT_EPISODE_ID.intValue())))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].type", is("fastForward")))
                .andExpect(jsonPath("$[0].data.startOffset", is(12)))
                .andExpect(jsonPath("$[0].data.endOffset", is(34)))
                .andExpect(jsonPath("$[0].data.speed", is(56.0)));
    }

    @Test
    public void createProgressEvent() throws Exception {
        Event event = makeProgressEvent(78);
        doPost(event);

        mvc.perform(get("/recent").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userId", is(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$[0].showId", is(DEFAULT_SHOW_ID.intValue())))
                .andExpect(jsonPath("$[0].episodeId", is(DEFAULT_EPISODE_ID.intValue())))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].type", is("progress")))
                .andExpect(jsonPath("$[0].data.offset", is(78)));
    }

    @Test
    public void createScrubEvent() throws Exception {
        Event event = makeScrubEvent(8, 9);
        doPost(event);

        mvc.perform(get("/recent").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userId", is(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$[0].showId", is(DEFAULT_SHOW_ID.intValue())))
                .andExpect(jsonPath("$[0].episodeId", is(DEFAULT_EPISODE_ID.intValue())))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].type", is("scrub")))
                .andExpect(jsonPath("$[0].data.startOffset", is(8)))
                .andExpect(jsonPath("$[0].data.endOffset", is(9)));
    }

    @Test
    public void createRewindEvent() throws Exception {
        Event event = makeRewindEvent(1, 2, 3);
        doPost(event);

        mvc.perform(get("/recent").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userId", is(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$[0].showId", is(DEFAULT_SHOW_ID.intValue())))
                .andExpect(jsonPath("$[0].episodeId", is(DEFAULT_EPISODE_ID.intValue())))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].type", is("rewind")))
                .andExpect(jsonPath("$[0].data.startOffset", is(1)))
                .andExpect(jsonPath("$[0].data.endOffset", is(2)))
                .andExpect(jsonPath("$[0].data.speed", is(3.0)));
    }

    @Test
    public void getReturnsRecent() throws Exception {
        doPost(makePlayEvent(1));
        doPost(makePauseEvent(2));
        doPost(makeFastForwardEvent(3, 4, 2.0));
        doPost(makeProgressEvent(5));
        doPost(makeScrubEvent(6, 7));
        doPost(makeRewindEvent(8, 9, 1.5));

        mvc.perform(get("/recent").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$..type", containsInAnyOrder("play", "pause", "fastForward", "progress", "scrub", "rewind")));
    }

    private Event makePlayEvent(int offset) {
        Long userId = DEFAULT_USER_ID;
        Long showId = DEFAULT_SHOW_ID;
        Long episodeId = DEFAULT_EPISODE_ID;
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("offset", offset);
        return new PlayEvent(userId, showId, episodeId, createdAt, dataPayload);
    }

    private Event makePauseEvent(int offset) {
        Long userId = DEFAULT_USER_ID;
        Long showId = DEFAULT_SHOW_ID;
        Long episodeId = DEFAULT_EPISODE_ID;
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("offset", offset);
        return new PauseEvent(userId, showId, episodeId, createdAt, dataPayload);
    }
    private Event makeProgressEvent(int offset) {
        Long userId = DEFAULT_USER_ID;
        Long showId = DEFAULT_SHOW_ID;
        Long episodeId = DEFAULT_EPISODE_ID;
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("offset", offset);
        return new ProgressEvent(userId, showId, episodeId, createdAt, dataPayload);
    }

    private Event makeFastForwardEvent(int startOffset, int endOffset, double speed) {
        Long userId = DEFAULT_USER_ID;
        Long showId = DEFAULT_SHOW_ID;
        Long episodeId = DEFAULT_EPISODE_ID;
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("startOffset", startOffset);
        dataPayload.put("endOffset", endOffset);
        dataPayload.put("speed", speed);
        return new FastForwardEvent(userId, showId, episodeId, createdAt, dataPayload);
    }

    private Event makeRewindEvent(int startOffset, int endOffset, double speed) {
        Long userId = DEFAULT_USER_ID;
        Long showId = DEFAULT_SHOW_ID;
        Long episodeId = DEFAULT_EPISODE_ID;
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("startOffset", startOffset);
        dataPayload.put("endOffset", endOffset);
        dataPayload.put("speed", speed);
        return new RewindEvent(userId, showId, episodeId, createdAt, dataPayload);
    }

    private Event makeScrubEvent(int startOffset, int endOffset) {
        Long userId = DEFAULT_USER_ID;
        Long showId = DEFAULT_SHOW_ID;
        Long episodeId = DEFAULT_EPISODE_ID;
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, Object> dataPayload = new HashMap<>();
        dataPayload.put("startOffset", startOffset);
        dataPayload.put("endOffset", endOffset);
        return new ScrubEvent(userId, showId, episodeId, createdAt, dataPayload);
    }

    private void doPost(Event event) throws Exception {
        mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(event)))
                .andExpect(status().isOk());
    }
}
