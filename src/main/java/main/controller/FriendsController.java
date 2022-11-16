package main.controller;

import main.api.response.CommonResponse;
import main.api.response.PersonResponse;
import main.errors.PersonNotFoundByEmailException;
import main.service.FriendsRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/v1/friends")
public class FriendsController {

    private final FriendsRecommendationService friendsRecommendationService;

    @Autowired
    public FriendsController(FriendsRecommendationService friendsRecommendationService) {
        this.friendsRecommendationService = friendsRecommendationService;
    }

    @GetMapping("/recommendations")
    @ResponseBody
    public ResponseEntity<CommonResponse<List<PersonResponse>>> getRecommendedFriends(Principal principal) throws PersonNotFoundByEmailException {
        List<PersonResponse> personResponses = friendsRecommendationService.getFriendsRecommendation(principal);
        return ResponseEntity.ok(CommonResponse.<List<PersonResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .total((long) personResponses.size())
                .data(personResponses)
                .build());
    }
}
