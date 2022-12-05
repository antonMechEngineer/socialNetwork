package main.controller;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/recommendations")
    @ResponseBody
    public CommonResponse<List<PersonResponse>> getRecommendedFriends() {
        return friendsRecommendationService.getFriendsRecommendation();
    }

    @PostMapping("/{id}")
    @ResponseBody
    public FriendshipRs sendFriendshipRequest (@PathVariable Long id) {
        return friendsService.sendFriendshipRequest(id);
    }

    @PostMapping("/request/{id}")
    @ResponseBody
    public FriendshipRs addFriend (@PathVariable Long id) {
        return friendsService.addFriend(id);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public FriendshipRs deleteFriend(@PathVariable Long id) {
        FriendshipRs friendshipRs = friendsService.deleteFriend(id);
        return friendshipRs;
    }

    @DeleteMapping("request/{id}")
    @ResponseBody
    public FriendshipRs deleteSentFriendshipRequest (@PathVariable Long id) {
        return friendsService.deleteSentFriendshipRequest(id);
    }

    @GetMapping()
    @ResponseBody
    public CommonResponse<List<PersonResponse>> getFriends(
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size
    )
    {
        return friendsService.getFriends(offset, size);
    }

    @GetMapping("/request")
    @ResponseBody
    public CommonResponse<List<PersonResponse>> getPotentialFriends(
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "perPage", required = false, defaultValue = "${socialNetwork.default.size}") int size
    )
    {
        return friendsService.getRequestedPersons(offset, size);
    }
}
