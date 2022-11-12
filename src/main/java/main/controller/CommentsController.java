package main.controller;

import main.api.request.CommentRequest;
import main.api.response.CommentsListResponse;
import main.errors.NoPostEntityException;
import main.model.entities.Post;
import main.model.entities.Comment;
import main.service.CommentsService;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/posts/{id}/comments")
public class CommentsController {

    private final PostsService postsService;
    private final CommentsService commentsService;

    @Autowired
    public CommentsController(PostsService postsService, CommentsService commentsService) {
        this.postsService = postsService;
        this.commentsService = commentsService;
    }

    @PostMapping
    public ResponseEntity<CommentsListResponse> createComment(@PathVariable long id, @RequestBody CommentRequest commentRequest) throws NoPostEntityException {
        Post post = postsService.findPostById(id);
        commentsService.createComment(post, commentRequest);
        Page<Comment> commentsPage = commentsService.getAllComments(0, 20);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentsListResponse(
                "success",
                LocalDateTime.now(),
                commentsPage.getNumber(),
                commentsPage.getSize(),
                new ArrayList<>(commentsPage.getContent()),
                ""
        ));
    }

    @GetMapping
    public ResponseEntity<CommentsListResponse> getComments(
            @PathVariable long id,
            @RequestParam(name = "page", required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${socialNetwork.default.size}") int size) {
        Page<Comment> commentsPage = commentsService.getAllComments(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentsListResponse(
                "success",
                LocalDateTime.now(),
                commentsPage.getNumber(),
                commentsPage.getSize(),
                new ArrayList<>(commentsPage.getContent()),
                ""
        ));
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<CommentsListResponse> deleteComment(@PathVariable long id, @PathVariable long comment_id) {
        commentsService.deleteComment(comment_id);
        Page<Comment> commentsPage = commentsService.getAllComments(0, 20);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentsListResponse(
                "success",
                LocalDateTime.now(),
                commentsPage.getNumber(),
                commentsPage.getSize(),
                new ArrayList<>(commentsPage.getContent()),
                ""
        ));
    }

    @PutMapping("/{comment_id}")
    public ResponseEntity<CommentsListResponse> editComment(@PathVariable long id, @PathVariable long comment_id) {
        commentsService.deleteComment(comment_id);
        Page<Comment> commentsPage = commentsService.getAllComments(0, 20);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentsListResponse(
                "success",
                LocalDateTime.now(),
                commentsPage.getNumber(),
                commentsPage.getSize(),
                new ArrayList<>(commentsPage.getContent()),
                ""
        ));
    }
}
