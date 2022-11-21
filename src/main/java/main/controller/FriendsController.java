package main.controller;

<<<<<<< HEAD
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

=======
import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.service.FriendsRecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsRecommendationService friendsRecommendationService;

    @GetMapping("/recommendations")
    @ResponseBody
    public CommonResponse<List<PersonResponse>> getRecommendedFriends() {
        return friendsRecommendationService.getFriendsRecommendation();
    }
>>>>>>> origin/dev
}
