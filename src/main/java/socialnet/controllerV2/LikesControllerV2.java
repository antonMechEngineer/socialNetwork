package socialnet.controllerV2;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import socialnet.aop.annotations.UpdateOnlineTime;
import socialnet.api.response.CommonRs;
import socialnet.api.response.ErrorRs;
import socialnet.api.response.LikeRs;
import socialnet.errors.NoSuchEntityException;
import socialnet.service.LikesService;

@RestController
@RequestMapping("/api/v2/likes")
@RequiredArgsConstructor
@Tag(name = "likes-controller", description = "Get likes, delete and put like")
public class LikesControllerV2 {

    private final LikesService likesService;

    @UpdateOnlineTime
    @GetMapping
    @ApiOperation(value = "get all my likes by comment or post")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<LikeRs> getLikesList(
            @RequestParam(name = "item_id") long itemId,
            @RequestParam String type) throws NoSuchEntityException {

        return likesService.getLikesResponse(itemId, type);
    }

//    @UpdateOnlineTime
//    @PutMapping
//    @ApiOperation(value = "put like on post or comment")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<LikeRs> putLike(
//            @RequestParam(name = "item_id") long itemId,
//            @RequestParam String type) {
//
//        return likesService.putLike(itemId, type);
//    }

    @UpdateOnlineTime
    @DeleteMapping
    @ApiOperation(value = "delete like from post or comment")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<LikeRs> deleteLike(
            @RequestParam(name = "item_id") long itemId,
            @RequestParam String type) throws NoSuchEntityException {

        return likesService.deleteLike(itemId, type);
    }
}
