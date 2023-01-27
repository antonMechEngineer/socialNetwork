package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.websocket.MessageTypingWs;
import soialNetworkApp.api.websocket.MessageWs;
import soialNetworkApp.kafka.MessagesKafkaProducer;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.security.jwt.JWTUtil;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageWsService {

    private final SimpMessagingTemplate messagingTemplate;
    private final DialogsService dialogsService;
    private final MessagesKafkaProducer messagesKafkaProducer;
    private final PersonsRepository personsRepository;
    private final MessagesRepository messagesRepository;
    private final JWTUtil jwtUtil;
    Long dialogId;
    String token;

    public void getMessageFromWs(MessageWs messageWs) {
        Long id = messageWs.getDialogId() + messageWs.getAuthorId() + System.currentTimeMillis();
        messageWs.setId(id);
        messageWs.setRecipientId(dialogsService.getRecipientFromDialog(messageWs.getAuthorId(), messageWs.getDialogId()).getId());
        messagingTemplate.convertAndSendToUser(messageWs.getDialogId().toString(), "/queue/messages", messageWs);
        messagesKafkaProducer.sendMessage(messageWs);
    }

    public void messageTypingFromWs(Long dialogId, String token, MessageTypingWs messageTypingWs) {
        if (this.dialogId == null && this.token == null) {
            this.dialogId = dialogId;
            this.token = token;
        }
        messagingTemplate.convertAndSendToUser(dialogId.toString(), "/queue/messages", messageTypingWs);
    }

    public void closeDialog() {
        log.info("exit dialog " + dialogId);
        Person user = personsRepository.findPersonByEmail(jwtUtil.extractUserName(token)).orElseThrow();
        log.info("user " + user.getId());
        messagesRepository.deleteAllByDialogIdAndAuthorIdAndIsDeletedTrue(dialogId, user.getId());
    }
}