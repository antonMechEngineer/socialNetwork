package main.controller;

import main.api.request.LoginRq;
import main.api.request.RegisterRq;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRq loginRq) {
        return userService.login(loginRq);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRq registerRq) {
        return "register ok";
    }
}
