//package socialnet.controllerV2;
//
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import socialnet.api.request.LoginRq;
//import socialnet.api.response.*;
//import socialnet.errors.PasswordException;
//import socialnet.errors.WrongEmailException;
//import socialnet.service.AuthService;
//import socialnet.service.CaptchaService;
//
//@RestController
//@RequestMapping("/api/v2/auth")
//@RequiredArgsConstructor
//@Tag(name = "auth-controller", description = "Working with captcha, login and logout")
//public class AuthController {
//
//    private final CaptchaService captchaService;
//    private final AuthService authService;
//
//    @GetMapping("/captcha")
//    @ApiOperation(value = "get captcha secret code and image url")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CaptchaRs captchaCheck() {
//        return captchaService.getCaptchaCode();
//    }
//
//    @PostMapping("/login")
//    @ApiOperation(value = "login by email and password")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<PersonRs> login(@RequestBody LoginRq loginRq) throws PasswordException, WrongEmailException {
//            return authService.loginUser(loginRq);
//    }
//
//    @PostMapping("/logout")
//    @ApiOperation(value = "logout current user")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<ComplexRs> logout() {
//        return authService.logoutUser();
//    }
//}
