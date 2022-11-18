package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRq;
import main.api.response.CaptchaRs;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.PersonResponse;
import main.service.AuthService;
import main.service.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CaptchaService captchaService;
    private final AuthService authService;

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaRs> captchaCheck() {
        return ResponseEntity.ok(captchaService.getCaptchaCode());
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<PersonResponse>> login(@RequestBody LoginRq loginRq) {
        Logger.getLogger(this.getClass().getName()).info("/api/v1/auth/login endpoint with request " + loginRq.getEmail() + " - " + loginRq.getPassword());
        CommonResponse<PersonResponse> commonResponse = authService.loginUser(loginRq);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<ComplexRs>> logout() {
        Logger.getLogger(this.getClass().getName()).info("/api/v1/auth/logout endpoint");
        return ResponseEntity.ok(authService.logoutUser());
    }
}
