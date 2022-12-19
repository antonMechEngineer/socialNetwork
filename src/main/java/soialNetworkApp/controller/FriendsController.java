package soialNetworkApp.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import soialNetworkApp.aop.annotations.UpdateOnlineTime;
import soialNetworkApp.api.response.CommonResponse;
import soialNetworkApp.api.response.ErrorRs;
import soialNetworkApp.api.response.FriendshipRs;
import soialNetworkApp.api.response.PersonResponse;
import soialNetworkApp.service.FriendsRecommendationService;
import soialNetworkApp.service.FriendsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
@Tag(name = "friends-controller",
        description = "Get recommended or potential friends. Add, delete, get friends. Send, delete friendship request")
public class FriendsController {

    private final FriendsService friendsService;
    private final FriendsRecommendationService friendsRecommendationService;

    @UpdateOnlineTime
    @GetMapping("/recommendations")
    @ApiOperation(value = "get recommendation friends")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<List<PersonResponse>> getRecommendedFriends() {
        return friendsRecommendationService.getFriendsRecommendation();
    }

    @UpdateOnlineTime
    @PostMapping("/{id}")
    @ApiOperation(value = "send friendship request by id of another user")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public FriendshipRs sendFriendshipRequest (@PathVariable Long id) throws Exception {
        return friendsService.sendFriendshipRequest(id);
    }

    @UpdateOnlineTime
    @PostMapping("/request/{id}")
    @ApiOperation(value = "add friend by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public FriendshipRs addFriend (@PathVariable Long id) throws Exception {
        return friendsService.addFriend(id);
    }

    @UpdateOnlineTime
    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete friend by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public FriendshipRs deleteFriend(@PathVariable Long id) throws Exception {
        return friendsService.deleteFriend(id);
    }

    @UpdateOnlineTime
    @DeleteMapping("request/{id}")
    @ApiOperation(value = "decline friendship request by id")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public FriendshipRs deleteSentFriendshipRequest (@PathVariable Long id) throws Exception {
        return friendsService.deleteSentFriendshipRequest(id);
    }

    @UpdateOnlineTime
    @GetMapping()
    @ApiOperation(value = "get friends of current user")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<List<PersonResponse>> getFriends(
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size
    ) throws Exception {
        return friendsService.getFriends(offset, size);
    }

    @UpdateOnlineTime
    @GetMapping("/request")
    @ApiOperation(value = "get potential friends of current user")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<List<PersonResponse>> getPotentialFriends(
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size
    ) throws Exception {
        return friendsService.getRequestedPersons(offset, size);
    }
}
