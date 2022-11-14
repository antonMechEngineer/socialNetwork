package main.controller;

import main.api.request.LoginRq;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public @ResponseBody String login(@RequestBody LoginRq loginRq, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "true");
        response.addCookie(cookie);
        return userService.jwtLogin(loginRq);
    }
}
