package main.controller;

import main.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendsController {
    private final FriendsService friendsService;

    @Autowired
    FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }


    @PostMapping ("/{id}")
    public String addFriend(
            @PathVariable Long id) {
        return "mock";
    }

    @DeleteMapping ("/{id}")
    public String deleteFriend(
            @PathVariable Long id) {
        return "mock";
    }

    @GetMapping("")
    public String getFriend() {
        return "mock";
    }


    //api/v1/friends/request
    //api/v1/friends/request/{id}
    ///api/v1/friends/request
    //api/v1/friends/recommendations

}
