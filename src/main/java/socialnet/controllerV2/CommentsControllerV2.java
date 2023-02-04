package socialnet.controllerV2;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import socialnet.aop.annotations.UpdateOnlineTime;
import socialnet.api.request.CommentRq;
import socialnet.api.response.CommentRs;
import socialnet.api.response.CommonRs;
import socialnet.api.response.ErrorRs;
import socialnet.service.CommentsService;
import socialnet.service.PostsService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/comments/{id}")
@RequiredArgsConstructor
@Tag(name = "comments-controller", description = "Create, delete, read, edit and recover comments")
public class CommentsControllerV2 {

    private final CommentsService commentsService;
    private final PostsService postsService;

    @UpdateOnlineTime
    @PostMapping
    @ApiOperation(value = "create comment")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<CommentRs> createComment(
            @ApiParam(value = "Parent post id") @PathVariable(name = "id") long postId,
            @RequestBody CommentRq commentRq) {

        return commentsService.createComment(postsService.findPostById(postId), commentRq);
    }

    @UpdateOnlineTime
    @GetMapping
    @ApiOperation(value = "get all comments by post")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<List<CommentRs>> getComments(
            @ApiParam(value = "Parent post id") @PathVariable(name = "id") long postId,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int page,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.size}") int size) {

        return commentsService.getPostComments(postsService.findPostById(postId), page, size);
    }

    @UpdateOnlineTime
    @DeleteMapping
    @ApiOperation(value = "delete comment by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<CommentRs> deleteComment(
            @ApiParam(value = "Deleting comment id") @PathVariable(name = "id") long commentId) {

        return commentsService.changeCommentDeleteStatus(commentId, true);
    }

    @UpdateOnlineTime
    @PatchMapping
    @ApiOperation(value = "recover comment by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<CommentRs> recoverComment(
            @ApiParam(value = "Recovering comment id") @PathVariable(name = "id") long commentId) {

        return commentsService.changeCommentDeleteStatus(commentId, false);
    }

    @UpdateOnlineTime
    @PutMapping
    @ApiOperation(value = "edit comment by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<CommentRs> editComment(
            @ApiParam(value = "Editing comment id") @PathVariable(name = "id") long commentId,
            @RequestBody CommentRq commentRq) {

        return commentsService.editComment(commentId, commentRq);
    }
}
