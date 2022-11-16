package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRequest;
import main.api.response.CaptchaRs;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.model.entities.Person;
import main.security.jwt.JWTUtil;
import main.service.AuthenticatesService;
import main.service.CaptchaService;
import main.service.PersonsService;
import main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CaptchaService captchaService;
    private final PersonsService personsService;
    private final AuthenticatesService authenticatesService;
    private final JWTUtil jwtUtil;

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaRs> captchaCheck() {
        return ResponseEntity.ok(captchaService.getCaptchaCode());
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<Person>> login(@RequestBody LoginRequest request) {
        Logger.getLogger(this.getClass().getName()).info("/api/v1/auth/login endpoint with request " + request.getEmail() + " - " + request.getPassword());
        Person person = personsService.getPersonByEmail(request.getEmail());
        if (person == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        person.setToken(jwtUtil.createToken(person.getEmail()));
        if (authenticatesService.validatePassword(person, request.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<Person>builder()
                    .error("success")
                    .errorDescription("")
                    .offset(0)
                    .perPage(0)
                    .timestamp(System.currentTimeMillis())
                    .data(person)
                    .build());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<ComplexRs>> logout() {
        Logger.getLogger(this.getClass().getName()).info("/api/v1/auth/logout endpoint");
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<ComplexRs>builder()
                .error("logout")
                .timestamp(System.currentTimeMillis())
                .offset(0)
                .perPage(0)
                .errorDescription("")
                .data(new ComplexRs())
                .build());
    }
}
