package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.response.CommonResponse;
import main.api.response.PostResponse;
import main.errors.NoPostEntityException;
import main.model.entities.Post;
import main.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/feeds")
    public ResponseEntity<CommonResponse<List<PostResponse>>> getFeeds(
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size) {
        Page<Post> postPage = postsService.getAllPosts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<PostResponse>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .total(postPage.getTotalElements())
                .itemPerPage(size)
                .offset(page)
                .data(new ArrayList<>(postsService.postsToResponse(postPage.getContent())))
                .errorDescription("")
                .build());
    }

    @PostMapping("/post")
    public ResponseEntity<CommonResponse<PostResponse>> createPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<PostResponse>builder()
                        .error("success")
                        .timestamp(System.currentTimeMillis())
                        .data(new PostResponse(postsService.createPost(postRequest)))
                        .build()
        );
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> getPost(@PathVariable int id) throws NoPostEntityException {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<PostResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(new PostResponse(postsService.findPostById(id)))
                .build()
        );
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> updatePost(@PathVariable int id, @RequestBody PostRequest postRequest) throws NoPostEntityException {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<PostResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(new PostResponse(postsService.updatePost(id, postRequest)))
                .build()
        );
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> deletePost(@PathVariable int id) throws NoPostEntityException {
        Post post = postsService.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<PostResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(new PostResponse(post))
                .build()
        );
    }
}
