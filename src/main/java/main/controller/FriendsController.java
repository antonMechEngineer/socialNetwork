package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.*;
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
        return friendsService.addFriend(token, id);
    }

    @DeleteMapping("/{id}")
    public FriendshipRs deleteFriend(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return friendsService.deleteFriend(token, id);
    }

    @GetMapping()
    public CommonResponse<List<PersonResponse>> getFriends(
            @RequestHeader("Authorization") String token)
//            @RequestParam(name = "page", required = false, defaultValue = "${socialNetwork.default.page}") int page,
//            @RequestParam(name = "size", required = false, defaultValue = "${socialNetwork.default.size}") int size)
    {
        return friendsService.getFriends(token, 0, 20);
    }

    @PostMapping("/request/{id}")
    public FriendshipRs sendFriendshipRequest(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id) {
        return friendsService.sendFriendshipRequest(token, id);
    }

    @GetMapping("/request")
    public CommonResponse<List<PersonResponse>> getPotentialFriends(
            @RequestHeader("Authorization") String token){
//            @RequestParam(name = "page", required = false) int page,
//            @RequestParam(name = "size", required = false) int size) {
        return friendsService.getRequestedPersons(token, 0, 20);
    }

    @DeleteMapping("request/{id}")
    public FriendshipRs deleteSentFriendshipRequest (@RequestHeader("Authorization") String token,
                                                     @PathVariable Long id) {
        return friendsService.deleteSentFriendshipRequest(token, id);
    }



}
