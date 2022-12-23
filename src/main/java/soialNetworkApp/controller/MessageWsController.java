package soialNetworkApp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import soialNetworkApp.api.request.MessageTypingWsRq;
import soialNetworkApp.api.request.MessageWsRq;
import soialNetworkApp.service.DialogsService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import soialNetworkApp.service.MessageWsService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageWsController {

    private final MessageWsService messageWsService;

    @MessageMapping("/dialogs/send_message")
    public void sendMessage(@Payload MessageWsRq messageWsRq) {
        messageWsService.getMessageFromWs(messageWsRq);
    }

    @MessageMapping("/dialogs/start_typing")
    public void startTyping(@Header("dialog_id") Long dialogId, @Header Long userId, @Payload MessageWsRq messageWsRq) {
        messageWsService.messageTypingFromWs(dialogId, userId, messageWsRq);
    }

    @MessageMapping("/dialogs/stop_typing")
    public void stopTyping(@Header("dialog_id") Long dialogId, @Header Long userId, @Payload MessageWsRq messageWsRq) {
        messageWsService.messageTypingFromWs(dialogId, userId, messageWsRq);
    }
}
