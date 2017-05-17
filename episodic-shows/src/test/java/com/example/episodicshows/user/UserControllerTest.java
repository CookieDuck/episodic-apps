package com.example.episodicshows.user;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(secure = false)
public class UserControllerTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    @Rollback
    public void testCreateUser() throws Exception {
        String expectedEmail = "Create@me.com";

        Map<String, Object> map = new HashMap<>();
        map.put("email", expectedEmail);
        String requestPayload = mapper.writeValueAsString(map);

        mvc.perform(post("/users")
                .content(requestPayload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email", is(expectedEmail)));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetUsers() throws Exception {
        userRepository.save(asList(new User("find@me.com"), new User("me@too.com")));

        mvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$..id").exists())
                .andExpect(jsonPath("$..email", containsInAnyOrder("find@me.com", "me@too.com")));
    }
}
