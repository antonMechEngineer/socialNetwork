package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.api.response.PostResponse;
import main.api.response.UserRs;
import main.errors.PersonNotFoundException;
import main.service.PersonsService;
import main.service.PostsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final PersonsService usersService;

    @GetMapping("/{id}")
    public CommonResponse<PersonResponse> getUserById(@PathVariable long id) {
        return usersService.getPersonDataById(id);
    }

    @GetMapping("/{id}/wall")
    public CommonResponse<List<PostResponse>> getUsersPosts(
            @PathVariable long id,
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "itemPerPage", required = false, defaultValue = "${socialNetwork.default.size}") int size) {

        return postsService.getAllPostsByAuthor(offset, size, usersService.getPersonById(id));
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
        return usersService.getMyData();
    }

    @PutMapping(value = "/me", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<UserRs> updateMyData(
            @RequestParam(value = "photo_id", required = false) MultipartFile photo,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "about", required = false) String about,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "first_name", required = false) String first_name,
            @RequestParam(value = "last_name", required = false) String last_name,
            @RequestParam(value = "birth_date", required = false) String birth_date,
            @RequestParam(value = "message_permission", required = false) String message_permission,
            Principal principal) throws IOException {
        return ResponseEntity
                .ok(usersService.editImage(principal, photo, phone, about, city, country, first_name,
                        last_name, birth_date, message_permission));
    }

    @DeleteMapping("/me")
    ResponseEntity<UserRs> deleteMyData() {
        return null;
    }
}
