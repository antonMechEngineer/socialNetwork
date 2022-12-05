package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.DialogRs;
import main.service.DialogsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/dialogs")
@RequiredArgsConstructor
public class DialogsController {

    private final DialogsService dialogsService;

    @GetMapping("/")
    public CommonResponse<List<DialogRs>> getDialogs() {
        return dialogsService.getAllDialogs();
    }


    @GetMapping("/unreaded")
    public ResponseEntity<?> unreaded() {
        return ResponseEntity.status(200).build();
    }
}
