package main.controller;

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
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterRs> register(@RequestBody RegisterRq regRequest) {
        return ResponseEntity.ok(accountService.getRegResponse(regRequest));
    }

    //@PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/password/set")
    public ResponseEntity<RegisterRs> passwordSet(@RequestBody PasswordRq passwordRq){return null;}

    //@PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/password/recovery")
    public ResponseEntity<RegisterRs> passwordRecovery(){return null;}

    //@PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/email")
    public ResponseEntity<RegisterRs> emailSet(@RequestBody EmailRq emailRq) {return null;}

    //@PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/email/recovery")
    public ResponseEntity<RegisterRs> emailRecovery() {return null;}
}
