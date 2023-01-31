package soialNetworkApp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import soialNetworkApp.api.websocket.MessageCommonWs;
import soialNetworkApp.api.websocket.MessageTypingWs;
import soialNetworkApp.api.websocket.MessageWs;
import org.springframework.stereotype.Controller;
import soialNetworkApp.service.MessageWsService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageWsController {

    private final MessageWsService messageWsService;

    @MessageMapping("/dialogs/send_message")
    public void sendMessage(@Payload MessageWs messageWs) {
        messageWsService.postMessage(messageWs);
    }

    @MessageMapping("/dialogs/start_typing")
    public void startTyping(@Header("dialog_id") Long dialogId, @Header String token, @Payload MessageTypingWs messageTypingWs) {
        messageWsService.messageTypingFromWs(dialogId, messageTypingWs);
    }

    @MessageMapping("/dialogs/stop_typing")
    public void stopTyping(@Header("dialog_id") Long dialogId, @Header String token, @Payload MessageTypingWs messageTypingWs) {
        messageWsService.messageTypingFromWs(dialogId, messageTypingWs);
    }

    @MessageMapping("/dialogs/edit_message")
    public void editMessage(@Payload MessageCommonWs message) {
        messageWsService.changeMessage(message);
    }

    @MessageMapping("/dialogs/delete_messages")
    public void deleteMessages(@Payload MessageCommonWs messages) {
        messageWsService.removeMessage(messages);
    }

    @MessageMapping("/dialogs/recover_message")
    public void recoverMessage(@Payload MessageCommonWs messageCommonWs) {
        messageWsService.restoreMessage(messageCommonWs);
    }

    @MessageMapping("/dialogs/close_dialog")
    public void closeDialog(@Payload MessageCommonWs messageCommonWs) {
        messageWsService.closeDialog(messageCommonWs);
    }
}
