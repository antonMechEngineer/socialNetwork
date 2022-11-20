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
        CommonResponse<PersonResponse> commonResponse = authService.loginUser(loginRq);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<ComplexRs>> logout() {
        return ResponseEntity.ok(authService.logoutUser());
    }
}
