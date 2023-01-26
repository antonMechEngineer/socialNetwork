package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.websocket.MessageTypingWs;
import soialNetworkApp.api.websocket.MessageWs;
import soialNetworkApp.kafka.MessagesKafkaProducer;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageWsService {

    private final SimpMessagingTemplate messagingTemplate;
    private final DialogsService dialogsService;
    private final MessagesKafkaProducer messagesKafkaProducer;

    public void getMessageFromWs(MessageWs messageWs) {
        log.info(messageWs.toString());
        Long id = messageWs.getDialogId() + messageWs.getAuthorId() + System.currentTimeMillis();
        messageWs.setId(id);
        messageWs.setRecipientId(dialogsService.getRecipientFromDialog(messageWs.getAuthorId(), messageWs.getDialogId()).getId());
        messagingTemplate.convertAndSendToUser(messageWs.getDialogId().toString(), "/queue/messages", messageWs);
        messagesKafkaProducer.sendMessage(messageWs);
    }

    public void messageTypingFromWs(Long dialogId, MessageTypingWs messageTypingWs) {
        log.info(messageTypingWs.toString());
        messagingTemplate.convertAndSendToUser(dialogId.toString(), "/queue/messages", messageTypingWs);
    }
}