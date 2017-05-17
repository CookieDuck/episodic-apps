package com.example.episodicshows.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return service.createUser(user);
    }
}
