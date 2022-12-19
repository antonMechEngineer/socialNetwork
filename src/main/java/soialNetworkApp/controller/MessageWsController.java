package soialNetworkApp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
