package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.MessagesRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagesKafkaConsumer {

    private final MessagesRepository messagesRepository;
    private final DialogsRepository dialogsRepository;


    @KafkaListener(topics = "messages", autoStartup = "${listen.auto.start:true}")
    public void consume(MessageKafka messageKafka) {
        log.info(String.format("Json received -> %s", messageKafka.toString()));
        if (messageKafka.getId() > 0) {
            Message message = messagesRepository.findById(messageKafka.getId()).orElseThrow();
            message.setReadStatus(ReadStatusTypes.READ);
            messagesRepository.save(message);
        } else {
            messagesRepository.save(messageKafka.getTime(), messageKafka.getMessageText(), messageKafka.getReadStatus().toString(),
                    messageKafka.getIsDeleted(), messageKafka.getAuthorId(), messageKafka.getRecipientId(), messageKafka.getDialogId());
            Message message = messagesRepository.findMessageByAuthorIdAndRecipientIdAndTime(
                    messageKafka.getAuthorId(), messageKafka.getRecipientId(), messageKafka.getTime()).orElseThrow();
            Dialog dialog = dialogsRepository.findById(messageKafka.getDialogId()).orElseThrow();
            dialog.setLastMessage(message);
            dialogsRepository.save(dialog);
        }
    }
}
