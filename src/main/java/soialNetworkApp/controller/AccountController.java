package soialNetworkApp.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.request.EmailRq;
import soialNetworkApp.api.request.PasswordRq;
import soialNetworkApp.api.request.PasswordSetRq;
import soialNetworkApp.api.request.RegisterRq;
import soialNetworkApp.api.response.*;
import soialNetworkApp.aop.annotations.UpdateOnlineTime;
import soialNetworkApp.api.request.*;
import soialNetworkApp.errors.CaptchaException;
import soialNetworkApp.errors.IncorrectRequestTypeException;
import soialNetworkApp.errors.PasswordException;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(name = "account-controller", description = "Working with password, email and registration")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    @ApiOperation(value = "user registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public ResponseEntity<RegisterRs> register(@RequestBody RegisterRq regRequest) throws PasswordException, CaptchaException {
        return ResponseEntity.ok(accountService.getRegResponse(regRequest));
    }

    @PutMapping("/password/set")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "set user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public ResponseEntity<RegisterRs> passwordSet(@RequestBody PasswordSetRq passwordSetRq){
        return ResponseEntity.ok(accountService.getPasswordSet(passwordSetRq));}

    @PutMapping("/password/reset")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "user password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public ResponseEntity<RegisterRs> passwordReSet(@RequestBody PasswordRq passwordRq){
        return ResponseEntity.ok(accountService.getPasswordReSet(passwordRq));}

    @PutMapping("/password/recovery")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "user password recovery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public ResponseEntity<RegisterRs> passwordRecovery(@RequestBody LinkedHashMap email){
        return ResponseEntity.ok(accountService.getPasswordRecovery(email.get("email").toString()));}

    @PutMapping("/email")
    @ApiOperation(value = "set email")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public ResponseEntity<RegisterRs> emailSet(@RequestBody EmailRq emailRq) {return null;}

    @PutMapping("/email/recovery")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation("user email recovery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public ResponseEntity<RegisterRs> emailRecovery(@RequestBody EmailRq emailRq) {
        return ResponseEntity.ok(accountService.getEmailRecovery());}

    @UpdateOnlineTime
    @GetMapping("/notifications")
    public CommonRs<List<PersonSettingsRs>> getPersonSettings() throws PersonNotFoundException {

        return accountService.getPersonSettings();
    }

    @UpdateOnlineTime
    @PutMapping("/notifications")
    public CommonRs<ComplexRs> editPersonSettings(
            @RequestBody PersonSettingsRq request) throws PersonNotFoundException, IncorrectRequestTypeException {

        return accountService.setPersonSetting(request);
    }
}
