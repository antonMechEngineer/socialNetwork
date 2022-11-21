package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
import main.api.response.CommentResponse;
import main.api.response.CommonResponse;
import main.service.CommentsService;
import main.service.PostsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post/{id}/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;
    private final PostsService postsService;

    @PostMapping
    public CommonResponse<CommentResponse> createComment(
            @PathVariable(name = "id") long postId,
            @RequestBody CommentRequest commentRequest) {

        return commentsService.createComment(postsService.findPostById(postId), commentRequest);
    }

    @GetMapping
    public CommonResponse<List<CommentResponse>> getComments(
            @PathVariable(name = "id") long postId,
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size) {

        return commentsService.getPostComments(postsService.findPostById(postId), offset, size);
    }

    @DeleteMapping("/{comment_id}")
    public CommonResponse<CommentResponse> deleteComment(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "comment_id") long commentId) {

        return commentsService.changeCommentDeleteStatus(commentId, true);
    }

    @PutMapping("/{comment_id}/recover")
    public CommonResponse<CommentResponse> recoverComment(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "comment_id") long commentId) {

        return commentsService.changeCommentDeleteStatus(commentId, false);
    }

    @PutMapping("/{comment_id}")
    public CommonResponse<CommentResponse> editComment(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "comment_id") long commentId,
            @RequestBody CommentRequest commentRequest) {

        return commentsService.editComment(commentId, commentRequest);
    }
}
