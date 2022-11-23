package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.request.UserRq;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.api.response.UserRs;
import main.errors.BadAuthorizationException;
import main.errors.PersonNotFoundException;
import main.service.PersonsService;
import main.service.PostsService;
import main.service.UsersService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final PostsService postsService;
    private final PersonsService personsService;
    private final UsersService usersService;

    @GetMapping("/{id}")
    public CommonResponse<PersonResponse> getUserById(@PathVariable long id) {
        return personsService.getPersonDataById(id);
    }




    @PutMapping("/me")
    ResponseEntity<UserRs> updateMyData(@RequestBody UserRq userRq) {
        return ResponseEntity
                .ok(usersService.editProfile(userRq));
    }

    @DeleteMapping("/me")
    ResponseEntity<UserRs> deleteMyData() {
        return null;
    }
}
