package main.controller;

import main.model.response.ListResponseRsPostRs;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PostsController {

    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/feeds")
    public ResponseEntity<ListResponseRsPostRs> getFeeds(
            @RequestParam(name = "page", required = false, defaultValue = "${team30.backend.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${team30.backend.size}") int size) {
        Page<Post> postList = postsService.getAllPosts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(new ListResponseRsPostRs(
                "success",
                System.currentTimeMillis(),
                postList.getTotalElements(),
                postList.getNumberOfElements(),
                postList.getContent(),
                page,
                ""
        ));
    }
}
