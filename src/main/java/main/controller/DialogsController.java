package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.DialogUserShortListDto;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.DialogRs;
import main.service.DialogsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/dialogs")
@RequiredArgsConstructor
public class DialogsController {

    private final DialogsService dialogsService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<DialogRs>>> dialogs() {
        return ResponseEntity.ok(dialogsService.getAllDialogs());
    }

    @PostMapping
    public ResponseEntity<CommonResponse<ComplexRs>> dialogsPreSend(@RequestBody DialogUserShortListDto dialogUserShortListDto) {
        return ResponseEntity.ok(dialogsService.getMessage());
    }

    @GetMapping("/unreaded")
    public ResponseEntity<CommonResponse<ComplexRs>> unreaded() {
        return ResponseEntity.ok(dialogsService.getMessage());
    }
}
