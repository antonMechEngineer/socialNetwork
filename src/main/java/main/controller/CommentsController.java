package main.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import main.AOP.annotations.UpdateOnlineTime;
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
@Tag(name = "comments-controller", description = "Create, delete, read, edit and recover comments")
public class CommentsController {

    private final CommentsService commentsService;
    private final PostsService postsService;

    @UpdateOnlineTime
    @PostMapping
    @ApiOperation(value = "create comment")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<CommentResponse> createComment(
            @PathVariable(name = "id") long postId,
            @RequestBody CommentRequest commentRequest) {

        return commentsService.createComment(postsService.findPostById(postId), commentRequest);
    }

    @UpdateOnlineTime
    @GetMapping
    @ApiOperation(value = "get comment by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<List<CommentResponse>> getComments(
            @PathVariable(name = "id") long postId,
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size) {

        return commentsService.getPostComments(postsService.findPostById(postId), offset, size);
    }

    @UpdateOnlineTime
    @DeleteMapping("/{comment_id}")
    @ApiOperation(value = "delete comment by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<CommentResponse> deleteComment(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "comment_id") long commentId) {

        return commentsService.changeCommentDeleteStatus(commentId, true);
    }

    @UpdateOnlineTime
    @PutMapping("/{comment_id}/recover")
    @ApiOperation(value = "recover comment by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<CommentResponse> recoverComment(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "comment_id") long commentId) {

        return commentsService.changeCommentDeleteStatus(commentId, false);
    }

    @UpdateOnlineTime
    @PutMapping("/{comment_id}")
    @ApiOperation(value = "edit comment by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<CommentResponse> editComment(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "comment_id") long commentId,
            @RequestBody CommentRequest commentRequest) {

        return commentsService.editComment(commentId, commentRequest);
    }
}
