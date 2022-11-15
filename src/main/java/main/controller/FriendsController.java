package main.controller;

import main.api.response.FriendRs;
import main.api.response.FriendsRs;
import main.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: 15.11.2022 в swagger 7 энпоинтов а здесь описаны только 6
@RestController
@RequestMapping("/api/v1/friends")
public class FriendsController {
    private final FriendsService friendsService;

    @Autowired
    FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }


    @PostMapping("/{id}")
    public ResponseEntity<FriendRs> addFriend(@PathVariable Long id) {
        return ResponseEntity.ok(friendsService.addFriend(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FriendRs> deleteFriend(@PathVariable Long id) {
        return ResponseEntity.ok(friendsService.deleteFriend(id));
    }

    @GetMapping()
    public ResponseEntity<FriendsRs> getFriend() {
        return ResponseEntity.ok(friendsService.getFriends());
    }


    @PostMapping("/request/{id}")
    public ResponseEntity<FriendRs> sendFriendshipRequest(@PathVariable Long id) {
        return ResponseEntity.ok(friendsService.sendFriendshipRequest(id));
    }

    @GetMapping("/request")
    public ResponseEntity<FriendsRs> getPotentialFriends() {
        return ResponseEntity.ok(friendsService.getPotentialFriends());
    }


    @GetMapping("/recommendations")
    public ResponseEntity<FriendsRs> getFriendRecommendations() {
        return ResponseEntity.ok(friendsService.getFriendRecommendations());
    }


}
