package main.controller;
import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.service.FriendsRecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import main.api.response.FriendshipRs;
import main.api.response.ListResponseRsPersonRs;
import main.service.FriendsService;

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
    public ListResponseRsPersonRs getFriends(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "page", required = false) int page,
            @RequestParam(name = "size", required = false) int size)
    {
        return friendsService.getFriends(token, page, size);
    }

    @PostMapping("/request/{id}")
    public FriendshipRs sendFriendshipRequest(@RequestHeader("Authorization") String token,
                                                              @PathVariable Long id) {
        return friendsService.sendFriendshipRequest(token, id);
    }

    @GetMapping("/request")
    public ListResponseRsPersonRs getPotentialFriends(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "page", required = false) int page,
            @RequestParam(name = "size", required = false) int size) {
        return friendsService.getRequestedPersons(token, page, size);
    }

    @DeleteMapping("/{id}")
    public FriendshipRs deleteSentFriendshipRequest (@RequestHeader("Authorization") String token,
                                                                     @PathVariable Long id) {
        return friendsService.deleteSentFriendshipRequest(token, id);
    }

}
