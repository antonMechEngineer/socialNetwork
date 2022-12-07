package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.DialogUserShortListDto;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.DialogRs;
import main.service.DialogsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dialogs")
@RequiredArgsConstructor
public class DialogsController {

    private final DialogsService dialogsService;

    @GetMapping
    public CommonResponse<List<DialogRs>> dialogs() {
        return dialogsService.getAllDialogs();
    }

    @PostMapping
    public CommonResponse<ComplexRs> dialogsStart(@RequestBody DialogUserShortListDto dialogUserShortListDto) {
        return dialogsService.beginDialog(dialogUserShortListDto);
    }

    @GetMapping("/unreaded")
    public CommonResponse<ComplexRs> unreaded() {
        return dialogsService.getMessage();
    }

    @PutMapping("/{dialogId}/read")
    public CommonResponse<ComplexRs> read() {
        return null;
    }
}
