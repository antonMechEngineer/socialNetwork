package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.websocket.MessageWs;
import soialNetworkApp.kafka.MessagesKafkaProducer;
import soialNetworkApp.mappers.DialogMapper;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.repository.PersonsRepository;

@Service
@RequiredArgsConstructor
public class MessageWsService {

    private final SimpMessagingTemplate messagingTemplate;
    private final DialogMapper dialogMapper;
    private final MessagesKafkaProducer messagesKafkaProducer;
    private final DialogsRepository dialogsRepository;
    private final PersonsRepository personsRepository;
    private final MessagesRepository messagesRepository;

    public void getMessageFromWs(MessageWs messageWs) {
        Person person = personsRepository.findById(messageWs.getAuthorId()).orElseThrow();
        Dialog dialog = dialogsRepository.findById(messageWs.getDialogId()).orElseThrow();
        Message message = dialogMapper.toMessageFromWs(messageWs, dialog, person);
        messagesRepository.save(message);
        dialog.setLastMessage(message);
        dialogsRepository.save(dialog);
        messageWs.setId(message.getId());
        messageWs.setRecipientId(message.getRecipient().getId());
        messagingTemplate.convertAndSendToUser(messageWs.getDialogId().toString(), "/queue/messages", messageWs);
//        messagesKafkaProducer.sendMessage(messageWs);
    }

    public void messageTypingFromWs(Long dialogId, Long userId, MessageWs messageWs) {
        messagingTemplate.convertAndSendToUser(dialogId.toString(), "/queue/messages",
                dialogMapper.toMessageTypingWs(userId, dialogId, messageWs.getTyping()));
    }
}
