package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.FriendshipRs;
import main.api.response.PersonResponse;
import main.service.FriendsRecommendationService;
import main.service.FriendsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;
    private final FriendsRecommendationService friendsRecommendationService;

    @GetMapping("/recommendations")
    @ResponseBody
    public CommonResponse<List<PersonResponse>> getRecommendedFriends() {
        return friendsRecommendationService.getFriendsRecommendation();
    }

    @PostMapping("/{id}")
    public FriendshipRs addFriend(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return friendsService.sendFriendshipRequest(token, id);
    }

    @DeleteMapping("/{id}")
    public FriendshipRs deleteFriend(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return friendsService.deleteFriend(token, id);
    }

    @GetMapping()
    @ResponseBody
    public CommonResponse<List<PersonResponse>> getFriends(
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.size}") int perPage,
            @RequestHeader("Authorization") String token) {
        return friendsService.getFriends(offset, perPage, token);
    }

    @PostMapping("/request/{id}")
    public FriendshipRs sendFriendshipRequest(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id) {
        return friendsService.addFriend(token, id);
    }

    @GetMapping("/request")
    @ResponseBody
    public CommonResponse<List<PersonResponse>> getPotentialFriends(
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.size}") int perPage,
            @RequestHeader("Authorization") String token) {
        return friendsService.getRequestedPersons(offset, perPage, token);
    }

    @DeleteMapping("request/{id}")
    public FriendshipRs deleteSentFriendshipRequest(@RequestHeader("Authorization") String token,
                                                    @PathVariable Long id) {
        return friendsService.deleteSentFriendshipRequest(token, id);
    }


}
