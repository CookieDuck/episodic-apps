package com.example.episodicshows.shows;

import com.example.episodicshows.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(secure = false)
public class ShowControllerTest {
    @Autowired
    ShowRepository showRepository;

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
    public void testGetUsers() throws Exception {
        showRepository.save(asList(new Show("South Park"), new Show("The Simpsons")));

        mvc.perform(get("/shows")
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$..id").exists())
                .andExpect(jsonPath("$..name", containsInAnyOrder("South Park", "The Simpsons")));
    }
}
