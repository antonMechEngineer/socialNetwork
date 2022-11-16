package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.response.CommonResponse;
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
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/feeds")
    public ResponseEntity<CommonResponse<List<Post>>> getFeeds(
            @RequestParam(name = "page", required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${socialNetwork.default.size}") int size) {
        Page<Post> postPage = postsService.getAllPosts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<Post>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .total(postPage.getTotalElements())
                .perPage(size)
                .offset(page)
                .data(postPage.getContent())
                .build());
    }

    @PostMapping
    public ResponseEntity<CommonResponse<List<Post>>> createPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<Post>>builder()
                        .error("success")
                        .timestamp(System.currentTimeMillis())
                        .data(new ArrayList<>(List.of(postsService.createPost(postRequest))))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<List<Post>>> getPost(@PathVariable int id) throws NoPostEntityException {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<Post>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(new ArrayList<>(List.of(postsService.findPostById(id))))
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<List<Post>>> updatePost(@PathVariable int id, @RequestBody PostRequest postRequest) throws NoPostEntityException {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<Post>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(new ArrayList<>(List.of(postsService.updatePost(id, postRequest))))
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Post>> deletePost(@PathVariable int id) throws NoPostEntityException {
        Post post = postsService.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<Post>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(new Post())
                .build()
        );
    }
}
