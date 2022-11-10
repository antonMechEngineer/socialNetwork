package main.controller;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.errors.NoPostEntityException;
import main.model.entities.Post;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostsController {

    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/feeds")
    public ResponseEntity<PostsListResponse> getFeeds(
            @RequestParam(name = "page", required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${socialNetwork.default.size}") int size) {
        Page<Post> postList = postsService.getAllPosts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(new PostsListResponse(
                "success",
                System.currentTimeMillis(),
                postList.getTotalElements(),
                postList.getNumberOfElements(),
                postList.getContent(),
                page,
                ""
        ));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponse(
                "success",
                System.currentTimeMillis(),
                0,
                0,
                new ArrayList<>(List.of(postsService.createPost(postRequest))),
                ""
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable int id) throws NoPostEntityException {
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponse(
                "success",
                System.currentTimeMillis(),
                0,
                0,
                new ArrayList<>(List.of(postsService.findPostById(id))),
                ""
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable int id, @RequestBody PostRequest postRequest) throws NoPostEntityException {
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponse(
                "success",
                System.currentTimeMillis(),
                0,
                0,
                new ArrayList<>(List.of(postsService.updatePost(id, postRequest))),
                ""
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable int id) {
        postsService.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponse(
                "success",
                System.currentTimeMillis(),
                0,
                0,
                new ArrayList<>(),
                ""
        ));
    }
}
