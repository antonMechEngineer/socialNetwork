package main.controller;

import main.api.request.RegisterRq;
import main.api.response.RegisterRs;
import main.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
