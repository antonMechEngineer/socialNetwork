package main.controller;

import main.api.request.LoginRq;
import main.api.response.LoginRs;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRs> login(@RequestBody LoginRq loginRq, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "true");
        response.addCookie(cookie);
        return ResponseEntity.ok(userService.jwtLogin(loginRq));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return null;
    }
}
