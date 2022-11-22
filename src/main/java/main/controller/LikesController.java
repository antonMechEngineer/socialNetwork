package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LikeRequest;
import main.api.response.CommonResponse;
import main.api.response.LikeResponse;
import main.service.LikesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping
    public CommonResponse<LikeResponse> getLikesList(
            @RequestParam(name = "item_id") long itemId,
            @RequestParam String type) {

        return likesService.getLikesByType(itemId, type);
    }

    @PutMapping
    public CommonResponse<LikeResponse> putLike(
            @RequestBody LikeRequest likeRequest) {

        return likesService.putLike(likeRequest);
    }

    @DeleteMapping
    public CommonResponse<LikeResponse> deleteLike(
            @RequestParam(name = "item_id") long itemId,
            @RequestParam String type) {

        return likesService.deleteLike(itemId, type);
    }
}
