package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.request.UserRq;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.api.response.PostResponse;
import main.api.response.UserRs;
import main.errors.PersonNotFoundException;
import main.service.PersonsService;
import main.service.PostsService;
import main.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final PostsService postsService;
    private final PersonsService personsService;
    private final UsersService usersService;

    @GetMapping("/{id}")
    public CommonResponse<PersonResponse> getUserById(@PathVariable long id,
                                                      @RequestHeader("Authorization") String token) {
        return personsService.getPersonDataById(id, token);
    }

    @GetMapping("/{id}/wall")
    public CommonResponse<List<PostResponse>> getUsersPosts(
            @PathVariable long id,
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "itemPerPage", required = false, defaultValue = "${socialNetwork.default.size}") int size) {

        return postsService.getAllPostsByAuthor(offset, size, personsService.getPersonById(id));
    }

    @PostMapping("/{id}/wall")
    public CommonResponse<PostResponse> createPost(
            @PathVariable(name = "id") Long personId,
            @RequestParam(name = "publish_date", required = false) Long publishingDate,
            @RequestBody PostRequest postRequest) throws PersonNotFoundException {

        return postsService.createPost(postRequest, personId, publishingDate);
    }

    @GetMapping("/me")
    public CommonResponse<PersonResponse> getMyData() {
        return personsService.getMyData();
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
