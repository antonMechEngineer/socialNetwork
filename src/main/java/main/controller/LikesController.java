package main.controller;

import main.api.request.LikeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
public class LikesController {

    @GetMapping
    public ResponseEntity<?> getLikesList(@RequestParam long item_id, @RequestParam String type) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping
    public ResponseEntity<?> putLike(@RequestBody LikeRequest likeRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLike(@RequestParam long item_id, @RequestParam String type) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
