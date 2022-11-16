package main.controller;

import main.api.request.UserRq;
import main.api.response.PostsListResponse;
import main.api.response.UserRs;
import main.model.entities.Post;
import main.service.PostsService;
import main.service.PersonsService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final PostsService postsService;
    private final PersonsService usersService;

    @Autowired
    public UsersController(PostsService postsService, PersonsService usersService) {
        this.postsService = postsService;
        this.usersService = usersService;
    }

    @GetMapping("/{id}/wall")
    public ResponseEntity<PostsListResponse> getUsersPosts(
            @PathVariable long id,
            @RequestParam(name = "page", required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${socialNetwork.default.size}") int size) {
        Page<Post> postPage = postsService.getAllPostsByAuthor(page, size, usersService.getPersonById(id));
        return ResponseEntity.status(HttpStatus.OK).body(new PostsListResponse(
                "success",
                System.currentTimeMillis(),
                postPage.getTotalElements(),
                page,
                postPage.getContent(),
                size,
                ""
        ));
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
