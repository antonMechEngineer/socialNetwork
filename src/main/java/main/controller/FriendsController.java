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
    public ResponseEntity<FriendshipRs> addFriend(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        return ResponseEntity.ok(friendsService.addFriend(token, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FriendshipRs> deleteFriend(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return ResponseEntity.ok(friendsService.deleteFriend(token, id));
    }

    @GetMapping()
    public ResponseEntity<ListResponseRsPersonRs> getFriend(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(friendsService.getFriends(token));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ListResponseRsPersonRs> getFriendRecommendations(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ListResponseRsPersonRs());
    }

    @PostMapping("/request/{id}")
    public ResponseEntity<FriendshipRs> sendFriendshipRequest(@RequestHeader("Authorization") String token,
                                                              @PathVariable Long id) {
        return ResponseEntity.ok(friendsService.sendFriendshipRequest(token, id));
    }

    @GetMapping("/request")
    public ResponseEntity<ListResponseRsPersonRs> getPotentialFriends(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(friendsService.getRequestedPersons(token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FriendshipRs> deleteSentFriendshipRequest (@RequestHeader("Authorization") String token,
                                                                     @PathVariable Long id) {
        return ResponseEntity.ok(friendsService.deleteSentFriendshipRequest(token, id));
    }

}
