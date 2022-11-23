package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.UserRq;
import main.api.response.UserRs;
import main.service.PersonsService;
import main.service.PostsService;
import main.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final PostsService postsService;
    private final PersonsService personsService;
    private final UsersService usersService;




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
