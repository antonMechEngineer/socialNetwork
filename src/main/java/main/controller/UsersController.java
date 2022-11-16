package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.UserRs;
import main.model.entities.Person;
import main.model.entities.Post;
import main.security.jwt.JWTUtil;
import main.service.PersonsService;
import main.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final PostsService postsService;
    private final PersonsService usersService;
    private final JWTUtil jwtUtil;

    @GetMapping("/{id}/wall")
    public ResponseEntity<CommonResponse<List<Post>>> getUsersPosts(
            @PathVariable long id,
            @RequestParam(name = "page", required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${socialNetwork.default.size}") int size) {
        Page<Post> postPage = postsService.getAllPostsByAuthor(page, size, usersService.getPersonById(id));
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<Post>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .offset(page)
                .perPage(size)
                .total(postPage.getTotalElements())
                .data(postPage.getContent())
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<Person>> getAuthorized(@RequestHeader(name = "Authorization") String auth) {
        Logger.getLogger(this.getClass().getName()).info("/api/v1/users/me endpoint with auth " + auth);
        Person person = new Person();
        if (jwtUtil.validateToken(auth)) {
            return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<Person>builder()
                    .error("success")
                    .timestamp(System.currentTimeMillis())
                    .data(person)
                    .build());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
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
            Principal principal)throws IOException { return ResponseEntity
            .ok(usersService.editImage(principal, photo,phone,about,city,country,first_name,
                    last_name,birth_date,message_permission));}

    @DeleteMapping("/me")
    ResponseEntity<UserRs>deleteMyData(){return null;}
}
