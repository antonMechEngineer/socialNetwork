package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRq;
import main.api.response.LoginRs;
import main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginRs> login(@RequestBody LoginRq loginRq) {
        return ResponseEntity.ok(userService.login(loginRq));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return null;
    }
}
