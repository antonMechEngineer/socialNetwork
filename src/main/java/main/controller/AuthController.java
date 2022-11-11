package main.controller;

import main.api.response.CaptchaRs;
import main.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final CaptchaService captchaService;

    @Autowired
    public AuthController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }
    @GetMapping("/captcha")
    public ResponseEntity<CaptchaRs> captchaCheck() {
        return ResponseEntity.ok(captchaService.getCaptchaCode());
    }
}
