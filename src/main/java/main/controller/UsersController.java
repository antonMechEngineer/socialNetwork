package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.UserRq;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.api.response.PostResponse;
import main.api.response.UserRs;
import main.model.entities.Post;
import main.security.jwt.JWTUtil;
import main.service.PersonsService;
import main.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<CommonResponse<List<PostResponse>>> getUsersPosts(
            @PathVariable long id,
            @RequestParam(name = "page", required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${socialNetwork.default.size}") int size) {
        Page<Post> postPage = postsService.getAllPostsByAuthor(page, size, usersService.getPersonById(id));
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<PostResponse>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .offset(page)
                .perPage(size)
                .total(postPage.getTotalElements())
                .data(new ArrayList<>(postsService.postsToResponse(postPage.getContent())))
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<PersonResponse>> getAuthorized(@RequestHeader(name = "Authorization") String auth) {
        Logger.getLogger(this.getClass().getName()).info("/api/v1/users/me endpoint with auth " + auth);
        if (jwtUtil.validateToken(auth)) {
            return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<PersonResponse>builder()
                    .error("success")
                    .timestamp(System.currentTimeMillis())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    //@PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/me")
    ResponseEntity<UserRs> updateMyData(@RequestBody UserRq userRq){return null;}

    //@PreAuthorize("hasAuthority('user:write')")
    @DeleteMapping("/me")
    ResponseEntity<UserRs>deleteMyData(){return null;}
}
