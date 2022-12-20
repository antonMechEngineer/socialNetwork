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

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageWsController {

    private final DialogsService dialogsService;

//    @MessageMapping("/dialogs/start_typing")
//    @MessageMapping("/dialogs/stop_typing")
//    @MessageMapping("/dialogs/mark_readed")


    @MessageMapping("/dialogs/send_message")
    public void sendMessage(@Payload MessageWsRq messageWsRq) {
        dialogsService.getMessageFromWs(messageWsRq);
    }

    @MessageMapping("/dialogs/start_typing")
    public void startTyping(@Header("dialog_id") Long dialogId, @Payload MessageWsRq messageWsRq) {
        dialogsService.messageTypingFromWs(dialogId, messageWsRq, true);
    }

    @MessageMapping("/dialogs/stop_typing")
    public void stopTyping(@Header("dialog_id") Long dialogId, @Payload MessageWsRq messageWsRq) {
        dialogsService.messageTypingFromWs(dialogId, messageWsRq, false);
    }
}
