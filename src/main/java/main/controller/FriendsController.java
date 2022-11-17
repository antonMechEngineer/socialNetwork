package main.controller;

import main.api.response.FriendshipRs;
import main.api.response.ListResponseRsPersonRs;
import main.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendsController {
    private final FriendsService friendsService;

    @Autowired
    FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
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
