package soialNetworkApp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
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
        messageWsService.getMessageFromWs(messageWs);
    }

    @MessageMapping("/dialogs/start_typing")
    public void startTyping(@Header("dialog_id") Long dialogId, @Header String token, @Payload MessageTypingWs messageTypingWs) {
        messageWsService.messageTypingFromWs(dialogId, token, messageTypingWs);
    }

    @MessageMapping("/dialogs/stop_typing")
    public void stopTyping(@Header("dialog_id") Long dialogId, @Header String token, @Payload MessageTypingWs messageTypingWs) {
        messageWsService.messageTypingFromWs(dialogId, token, messageTypingWs);
    }

    @MessageMapping("/dialogs/close")
    public void close() {
        messageWsService.closeDialog();
    }
}
