package main.controller;

import lombok.RequiredArgsConstructor;
import main.AOP.annotations.UpdateOnlineTime;
import main.api.request.*;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.PersonSettingsResponse;
import main.api.response.RegisterRs;
import main.errors.IncorrectRequestTypeException;
import main.errors.PersonNotFoundException;
import main.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

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

    @UpdateOnlineTime
    @GetMapping("/notifications")
    public CommonResponse<List<PersonSettingsResponse>> getPersonSettings() throws PersonNotFoundException {

        return accountService.getPersonSettings();
    }

    @UpdateOnlineTime
    @PutMapping("/notifications")
    public CommonResponse<ComplexRs> editPersonSettings(
            @RequestBody PersonSettingsRequest request) throws PersonNotFoundException, IncorrectRequestTypeException {

        return accountService.setPersonSetting(request);
    }
}
