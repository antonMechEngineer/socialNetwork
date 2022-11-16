package main.controller;

import main.api.response.FriendRs;
import main.api.response.FriendsRs;
import main.security.jwt.JWTUtil;
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
    public ResponseEntity<FriendRs> addFriend(@RequestHeader("Authorization") String token, @PathVariable Long id) {

        return ResponseEntity.ok(friendsService.addFriend(token, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FriendRs> deleteFriend(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return ResponseEntity.ok(friendsService.deleteFriend(token, id));
    }

    @GetMapping()
    public ResponseEntity<FriendsRs> getFriend(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(friendsService.getFriends(token));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<FriendsRs> getFriendRecommendations(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new FriendsRs());
    }

    @PostMapping("/request/{id}")
    public ResponseEntity<FriendRs> sendFriendshipRequest(@RequestHeader("Authorization") String token,
                                                          @PathVariable Long id) {
        return ResponseEntity.ok(friendsService.sendFriendshipRequest(token, id));
    }

    @GetMapping("/request")
    public ResponseEntity<FriendsRs> getPotentialFriends(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(friendsService.getPotentialFriends(token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FriendRs> deleteSentFriendshipRequest (@RequestHeader("Authorization") String token,
                                                                 @PathVariable Long id) {
        return ResponseEntity.ok(friendsService.deleteSentFriendshipRequest(token, id));
    }

}
