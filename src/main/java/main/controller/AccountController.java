package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.EmailRq;
import main.api.request.PasswordRq;
import main.api.request.PasswordSetRq;
import main.api.request.RegisterRq;
import main.api.response.RegisterRs;
import main.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<RegisterRs> register(@RequestBody RegisterRq regRequest) {
        return ResponseEntity.ok(accountService.getRegResponse(regRequest));
    }

    @PutMapping("/password/set")
    public ResponseEntity<RegisterRs> passwordSet(@RequestBody PasswordSetRq passwordSetRq){
        return ResponseEntity.ok(accountService.getPasswordSet(passwordSetRq));}

    @PutMapping("/password/reset")
    public ResponseEntity<RegisterRs> passwordReSet(@RequestBody PasswordRq passwordRq){
        return ResponseEntity.ok(accountService.getPasswordReSet(passwordRq));}

    @PutMapping("/password/recovery")
    public ResponseEntity<RegisterRs> passwordRecovery(@RequestBody LinkedHashMap email){
        return ResponseEntity.ok(accountService.getPasswordRecovery(email.get("email").toString()));}

    @PutMapping("/email")
    public ResponseEntity<RegisterRs> emailSet(@RequestBody EmailRq emailRq) {return null;}

    @PutMapping("/email/recovery")
    public ResponseEntity<RegisterRs> emailRecovery(@RequestBody EmailRq emailRq) {
        return ResponseEntity.ok(accountService.getEmailRecovery());}
}
