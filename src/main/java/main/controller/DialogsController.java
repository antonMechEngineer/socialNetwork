package main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.api.request.DialogUserShortListDto;
import main.api.request.MessageRq;
import main.api.response.CommonResponse;
import main.api.response.ComplexRs;
import main.api.response.DialogRs;
import main.api.response.MessageRs;
import main.service.DialogsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @GetMapping("/{dialogId}/messages")
    public CommonResponse<List<MessageRs>> messages(@PathVariable Long dialogId) {
        return dialogsService.getMessages(dialogId);
    }

    @PostMapping("/{dialogId}/messages")
    public CommonResponse<MessageRs> messagesPost(@PathVariable Long dialogId, @RequestBody MessageRq messageRq) {
        return dialogsService.getLastMessageRs(dialogId, messageRq);
    }

    @GetMapping("/unreaded")
    public CommonResponse<ComplexRs> unread() {
        return dialogsService.getUnreadMessage();
    }

    @PutMapping("/{dialogId}/read")
    public CommonResponse<ComplexRs> read(@PathVariable Long dialogId) {
        return null;
    }
}
