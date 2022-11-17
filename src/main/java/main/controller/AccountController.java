package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.EmailRq;
import main.api.request.PasswordRq;
import main.api.request.RegisterRq;
import main.api.response.RegisterRs;
import main.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<RegisterRs> register(@RequestBody RegisterRq regRequest) {System.out.println(regRequest.getCode());
        return ResponseEntity.ok(accountService.getRegResponse(regRequest));
    }

    @PutMapping("/password/set")
    public ResponseEntity<RegisterRs> passwordSet(@RequestBody PasswordRq passwordRq){return null;}

    @PutMapping("/password/recovery")
    public ResponseEntity<RegisterRs> passwordRecovery(){return null;}

    @PutMapping("/email")
    public ResponseEntity<RegisterRs> emailSet(@RequestBody EmailRq emailRq) {return null;}

    @PutMapping("/email/recovery")
    public ResponseEntity<RegisterRs> emailRecovery() {return null;}
}
