package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRq;
import main.api.response.CaptchaRs;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.PersonResponse;
import main.service.AuthService;
import main.service.CaptchaService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CaptchaService captchaService;
    private final AuthService authService;

    @GetMapping("/captcha")
    public CaptchaRs captchaCheck() {
        return captchaService.getCaptchaCode();
    }

    @PostMapping("/login")
    public CommonResponse<PersonResponse> login(@RequestBody LoginRq loginRq) throws BadCredentialsException {
            return authService.loginUser(loginRq);
    }

    @PostMapping("/logout")
    public CommonResponse<ComplexRs> logout() {
        return authService.logoutUser();
    }
}
