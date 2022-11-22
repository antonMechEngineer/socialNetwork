package main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class NotificationsController {

    @GetMapping("/notifications")
    public ResponseEntity<?> notifications() {
        return ResponseEntity.status(200).build();
    }
}
