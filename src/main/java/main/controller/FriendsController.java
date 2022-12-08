package main.controller;

import lombok.RequiredArgsConstructor;
import main.AOP.annotations.UpdateOnlineTime;
import main.api.response.CommonResponse;
import main.api.response.FriendshipRs;
import main.api.response.PersonResponse;
import main.service.FriendsRecommendationService;
import main.service.FriendsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;
    private final FriendsRecommendationService friendsRecommendationService;

    @UpdateOnlineTime
    @GetMapping("/recommendations")
    public CommonResponse<List<PersonResponse>> getRecommendedFriends() {
        return friendsRecommendationService.getFriendsRecommendation();
    }

    @UpdateOnlineTime
    @PostMapping("/{id}")
    public FriendshipRs sendFriendshipRequest (@PathVariable Long id) throws Exception {
        return friendsService.sendFriendshipRequest(id);
    }

    @UpdateOnlineTime
    @PostMapping("/request/{id}")
    public FriendshipRs addFriend (@PathVariable Long id) throws Exception {
        return friendsService.addFriend(id);
    }

    @UpdateOnlineTime
    @DeleteMapping("/{id}")
    public FriendshipRs deleteFriend(@PathVariable Long id) throws Exception {
        FriendshipRs friendshipRs = friendsService.deleteFriend(id);
        return friendshipRs;
    }

    @UpdateOnlineTime
    @DeleteMapping("request/{id}")
    public FriendshipRs deleteSentFriendshipRequest (@PathVariable Long id) throws Exception {
        return friendsService.deleteSentFriendshipRequest(id);
    }

    @UpdateOnlineTime
    @GetMapping()
    public CommonResponse<List<PersonResponse>> getFriends(
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size
    ) throws Exception {
        return friendsService.getFriends(offset, size);
    }

    @UpdateOnlineTime
    @GetMapping("/request")
    public CommonResponse<List<PersonResponse>> getPotentialFriends(
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size
    ) throws Exception {
        return friendsService.getRequestedPersons(offset, size);
    }
}
