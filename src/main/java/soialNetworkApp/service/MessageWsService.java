package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.websocket.MessageCommonWs;
import soialNetworkApp.api.websocket.MessageTypingWs;
import soialNetworkApp.api.websocket.MessageWs;
import soialNetworkApp.kafka.MessagesKafkaProducer;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.security.jwt.JWTUtil;

import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageWsService {

    private final SimpMessagingTemplate messagingTemplate;
    private final DialogsService dialogsService;
    private final MessagesKafkaProducer messagesKafkaProducer;
    private final MessagesRepository messagesRepository;
    private final DialogsRepository dialogsRepository;
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

    public void changeMessage(MessageCommonWs messageCommonWs) {
        Message message = messagesRepository.findById(messageCommonWs.getMessageId()).orElseThrow();
        message.setMessageText(messageCommonWs.getMessageText());
        messagesRepository.save(message);
    }

    public void removeMessage(MessageCommonWs messages) {
        Dialog dialog = dialogsRepository.findById(messages.getDialogId()).orElseThrow();
        messages.getMessageIds()
                .forEach(id -> {
                    log.info(id.toString());
                    Message message = messagesRepository.findById(id).orElse(null);
                    if (message != null) {
                        if (message.getId().equals(dialog.getLastMessage().getId())) {
                            dialog.setLastMessage(null);
                            dialogsRepository.save(dialog);
                        }
                        message.setIsDeleted(true);
                        messagesRepository.save(message);
                        if (dialog.getLastMessage() == null) {
                            dialog.setLastMessage(getLastMessage(messages.getDialogId()));
                            dialogsRepository.save(dialog);
                        }
                    }
                });
    }

    public void restoreMessage(MessageCommonWs messageCommonWs) {
        Message message = messagesRepository.findById(messageCommonWs.getMessageId()).orElseThrow();
        message.setIsDeleted(false);
        messagesRepository.save(message);
    }

    public void closeDialog(MessageCommonWs messageCommonWs) {
        log.info("exit dialog " + messageCommonWs.getDialogId());
        log.info("user " + messageCommonWs.getUserId());
        messagesRepository.deleteAllByDialogIdAndAuthorIdAndIsDeletedTrue(messageCommonWs.getDialogId(), messageCommonWs.getUserId());
    }

    private Message getLastMessage(Long dialogId) {
        return messagesRepository.findAllByDialogIdAndIsDeletedFalse(dialogId)
                .stream()
                .max(Comparator.comparing(Message::getTime)).orElse(null);
    }
}