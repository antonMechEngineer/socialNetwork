package main.controller;

import lombok.RequiredArgsConstructor;
import main.AOP.annotations.UpdateOnlineTime;
import main.api.request.FindPersonRq;
import main.api.request.PostRequest;
import main.api.request.UserRq;
import main.api.response.*;
import main.errors.EmptyFieldException;
import main.errors.PersonNotFoundException;
import main.service.PersonCacheService;
import main.service.PersonsService;
import main.service.PostsService;
import main.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@UpdateOnlineTime
public class UsersController {

    private final PostsService postsService;
    private final PersonsService personsService;
    private final UsersService usersService;
    private final PersonCacheService personCacheService;

    @UpdateOnlineTime
    @GetMapping("/{id}")
    public CommonResponse<PersonResponse> getUserById(@PathVariable long id,
                                                      @RequestHeader("Authorization") String token) {
        return personsService.getPersonDataById(id, token);
    }

    @UpdateOnlineTime
    @GetMapping("/{id}/wall")
    public CommonResponse<List<PostResponse>> getUsersPosts(
            @PathVariable long id,
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "itemPerPage", required = false, defaultValue = "${socialNetwork.default.size}") int size) {

        return postsService.getAllPostsByAuthor(offset, size, personCacheService.getPersonById(id));
    }

    @UpdateOnlineTime
    @PostMapping("/{id}/wall")
    public CommonResponse<PostResponse> createPost(
            @PathVariable(name = "id") Long personId,
            @RequestParam(name = "publish_date", required = false) Long publishingDate,
            @RequestBody PostRequest postRequest) throws PersonNotFoundException {

        return postsService.createPost(postRequest, personId, publishingDate);
    }

    @UpdateOnlineTime
    @GetMapping("/me")
    public CommonResponse<PersonResponse> getMyData() {
        return personsService.getMyData();
    }

    @UpdateOnlineTime
    @PutMapping("/me")
    ResponseEntity<UserRs> updateMyData(@RequestBody UserRq userRq) {
        return ResponseEntity
                .ok(usersService.editProfile(userRq));
    }

    @DeleteMapping("/me")
    ResponseEntity<ResponseRsComplexRs> deleteMyData() {
        return ResponseEntity
                .ok(usersService.deleteProfile());
    }
    @PostMapping("/me/recover")
    ResponseEntity<ResponseRsComplexRs> recoverMyData() {
        return ResponseEntity
                .ok(usersService.recoverProfile());
    }

    @UpdateOnlineTime
    @GetMapping("/search")
    public CommonResponse<List<PersonResponse>> findPersons(
            FindPersonRq personRq,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.size}") int perPage) throws SQLException, EmptyFieldException {
        return usersService.findPersons(personRq, offset, perPage);
    }
}
