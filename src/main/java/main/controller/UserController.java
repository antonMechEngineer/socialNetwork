package main.controller;

import main.model.entities.User;
import main.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<User> getAllUsers() {
        return this.userService.getAll();
    }
}
