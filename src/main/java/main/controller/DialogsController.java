package main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/dialogs")
@RequiredArgsConstructor
public class DialogsController {

    @GetMapping("/unreaded")
    public ResponseEntity<?> unreaded() {
        return ResponseEntity.status(200).build();
    }
}
