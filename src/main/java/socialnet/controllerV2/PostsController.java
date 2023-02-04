//package socialnet.controllerV2;
//
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import socialnet.aop.annotations.UpdateOnlineTime;
//import socialnet.api.request.FindPostRq;
//import socialnet.api.request.PostRq;
//import socialnet.api.response.CommonRs;
//import socialnet.api.response.ErrorRs;
//import socialnet.api.response.PostRs;
//import socialnet.errors.EmptyFieldException;
//import socialnet.errors.PersonNotFoundException;
//import socialnet.service.PostsService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v2/posts")
//@RequiredArgsConstructor
//@Tag(name = "posts-controller", description = "Get feeds. Get, update, delete, recover, find post")
//public class PostsController {
//
//    private final PostsService postsService;
//
//    @UpdateOnlineTime
//    @GetMapping("/feeds")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiOperation(value = "get all news")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<List<PostRs>> getFeeds(
//            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int page,
//            @RequestParam(required = false, defaultValue = "${socialNetwork.default.size}") int size) {
//
//        return postsService.getFeeds(page, size);
//    }
//
//    @UpdateOnlineTime
//    @GetMapping("/{id}")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiOperation(value = "get post by id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<PostRs> getPost(
//            @PathVariable(name = "id") Long postId) {
//
//        return postsService.getPostById(postId);
//    }
//
//    @UpdateOnlineTime
//    @PutMapping("/{id}")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiOperation(value = "update post by id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<PostRs> updatePost(
//            @PathVariable int id,
//            @RequestBody PostRq postRq) throws PersonNotFoundException {
//
//        return postsService.updatePost(id, postRq);
//    }
//
//    @UpdateOnlineTime
//    @DeleteMapping("/{id}")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiOperation(value = "delete post by id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<PostRs> deletePost(
//            @PathVariable long id) throws PersonNotFoundException {
//
//        return postsService.changeDeleteStatusInPost(id, true);
//    }
//
//    @UpdateOnlineTime
//    @PatchMapping("/{id}")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiOperation(value = "recover post by id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<PostRs> recoverPost(
//            @PathVariable long id) throws PersonNotFoundException {
//
//        return postsService.changeDeleteStatusInPost(id, false);
//    }
//
//    @UpdateOnlineTime
//    @GetMapping
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiOperation(value = "find all posts by query")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<List<PostRs>> findPost(
//            FindPostRq postRq,
//            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int page,
//            @RequestParam(required = false, defaultValue = "${socialNetwork.default.size}") int size) throws EmptyFieldException {
//        return postsService.findPosts(postRq, page, size);
//    }
//}
