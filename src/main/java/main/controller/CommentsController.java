package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
import main.api.response.CommentResponse;
import main.api.response.CommonResponse;
import main.errors.NoPostEntityException;
import main.model.entities.Comment;
import main.model.entities.Post;
import main.service.CommentsService;
import main.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{id}/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final PostsService postsService;
    private final CommentsService commentsService;

    @PostMapping
    public ResponseEntity<CommonResponse<List<CommentResponse>>> createComment(@PathVariable long id, @RequestBody CommentRequest commentRequest) throws NoPostEntityException {
        Post post = postsService.findPostById(id);
        commentsService.createComment(post, commentRequest);
        Page<Comment> commentsPage = commentsService.getAllComments(0, 20);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<CommentResponse>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .offset(commentsPage.getNumber())
                .perPage(commentsPage.getSize())
                .total(commentsPage.getTotalElements())
                .data(new ArrayList<>(commentsService.commentsToResponse(commentsPage.getContent())))
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CommentResponse>>> getComments(
            @PathVariable long id,
            @RequestParam(name = "page", required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${socialNetwork.default.size}") int size) {
        Page<Comment> commentsPage = commentsService.getAllComments(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<CommentResponse>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .offset(commentsPage.getNumber())
                .perPage(commentsPage.getSize())
                .total(commentsPage.getTotalElements())
                .data(new ArrayList<>(commentsService.commentsToResponse(commentsPage.getContent())))
                .build()
        );
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<CommonResponse<List<CommentResponse>>> deleteComment(@PathVariable long id, @PathVariable long comment_id) {
        commentsService.deleteComment(comment_id);
        Page<Comment> commentsPage = commentsService.getAllComments(0, 20);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<CommentResponse>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .offset(commentsPage.getNumber())
                .perPage(commentsPage.getSize())
                .total(commentsPage.getTotalElements())
                .data(new ArrayList<>(commentsService.commentsToResponse(commentsPage.getContent())))
                .build()
        );
    }

    @PutMapping("/{comment_id}")
    public ResponseEntity<CommonResponse<List<CommentResponse>>> editComment(
            @PathVariable long id, @PathVariable long comment_id, @RequestBody CommentRequest commentRequest) {
        commentsService.editComment(comment_id, commentRequest.getCommentText());
        Page<Comment> commentsPage = commentsService.getAllComments(0, 20);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<List<CommentResponse>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .offset(commentsPage.getNumber())
                .perPage(commentsPage.getSize())
                .total(commentsPage.getTotalElements())
                .data(new ArrayList<>(commentsService.commentsToResponse(commentsPage.getContent())))
                .build()
        );
    }
}
