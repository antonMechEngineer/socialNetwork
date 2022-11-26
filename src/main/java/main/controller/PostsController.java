package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.FindPostRq;
import main.api.request.PostRequest;
import main.api.response.CommonResponse;
import main.api.response.PostResponse;
import main.errors.EmptyFieldException;
import main.errors.PersonNotFoundException;
import main.service.PostsService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/feeds")
    public CommonResponse<List<PostResponse>> getFeeds(
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size) {

        return postsService.getFeeds(offset, size);
    }

    @GetMapping("/post/{id}")
    public CommonResponse<PostResponse> getPost(
            @PathVariable(name = "id") Long postId) {

        return postsService.getPostById(postId);
    }

    @PutMapping("/post/{id}")
    public CommonResponse<PostResponse> updatePost(
            @PathVariable int id,
            @RequestBody PostRequest postRequest) throws PersonNotFoundException {

        return postsService.updatePost(id, postRequest);
    }

    @DeleteMapping("/post/{id}")
    public CommonResponse<PostResponse> deletePost(
            @PathVariable long id) throws PersonNotFoundException {

        return postsService.changeDeleteStatusInPost(id, true);
    }

    @PutMapping("/post/{id}/recover")
    public CommonResponse<PostResponse> recoverPost(
            @PathVariable long id) throws PersonNotFoundException {

        return postsService.changeDeleteStatusInPost(id, false);
    }

    @GetMapping("/post")
    public CommonResponse<List<PostResponse>> findPost(FindPostRq postRq,
                                                       @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int offset,
                                                       @RequestParam(required = false, defaultValue = "${socialNetwork.default.size}") int perPage) throws SQLException, EmptyFieldException {

        return postsService.findPosts(postRq, offset, perPage);
    }
}
