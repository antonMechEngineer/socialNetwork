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
//import socialnet.api.response.CommonRs;
//import socialnet.api.response.ComplexRs;
//import socialnet.api.response.ErrorRs;
//import socialnet.api.response.PersonRs;
//import socialnet.errors.PersonException;
//import socialnet.service.FriendsRecommendationService;
//import socialnet.service.FriendsService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v2/friends")
//@RequiredArgsConstructor
//@Tag(name = "friends-controller",
//        description = "Get recommended or potential friends. Add, delete, get friends. Send, delete friendship request")
//public class FriendsController {
//
//    private final FriendsService friendsService;
//    private final FriendsRecommendationService friendsRecommendationService;
//
//    @UpdateOnlineTime
//    @GetMapping("/recommendations")
//    @ApiOperation(value = "get recommendation friends")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<List<PersonRs>> getRecommendedFriends() {
//        return friendsRecommendationService.getFriendsRecommendation();
//    }
//
//    @UpdateOnlineTime
//    @PostMapping("/{id}")
//    @ApiOperation(value = "send friendship request by id of another user")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<ComplexRs> sendFriendshipRequest (@PathVariable Long id) throws Exception {
//        return friendsService.sendFriendshipRequest(id);
//    }
//
//    @UpdateOnlineTime
//    @PostMapping("/request/{id}")
//    @ApiOperation(value = "add friend by id")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<ComplexRs> addFriend (@PathVariable Long id) throws Exception {
//        return friendsService.addFriend(id);
//    }
//
//    @UpdateOnlineTime
//    @DeleteMapping("/{id}")
//    @ApiOperation(value = "delete friend by id")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<ComplexRs> deleteFriend(@PathVariable Long id) throws Exception {
//        return friendsService.deleteFriend(id);
//    }
//
//    @UpdateOnlineTime
//    @DeleteMapping("request/{id}")
//    @ApiOperation(value = "decline friendship request by id")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<ComplexRs> deleteSentFriendshipRequest (@PathVariable Long id) throws Exception {
//        return friendsService.deleteSentFriendshipRequest(id);
//    }
//
//    @UpdateOnlineTime
//    @GetMapping()
//    @ApiOperation(value = "get friends of current user")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<List<PersonRs>> getFriends(
//            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
//            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size
//    ) throws Exception {
//        return friendsService.getFriends(offset, size);
//    }
//
//    @UpdateOnlineTime
//    @GetMapping("/request")
//    @ApiOperation(value = "get potential friends of current user")
//    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "forbidden")
//    })
//    public CommonRs<List<PersonRs>> getPotentialFriends(
//            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
//            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size
//    ) throws Exception {
//        return friendsService.getRequestedPersons(offset, size);
//    }
//
//    @PostMapping("/block_unblock")
//    public void userBlocksUser(@RequestParam(value = "block_user_id") Long blockUserId) throws PersonException {
//        friendsService.userBlocksUser(blockUserId);
//    }
//}
