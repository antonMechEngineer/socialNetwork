package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.MessagesRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagesKafkaConsumer {

    private final MessagesRepository messagesRepository;

    @KafkaListener(topics = "messages", groupId = "myGroup")
    public void consume(MessageKafka messageKafka) {
        log.info(String.format("Json received -> %s", messageKafka.toString()));
        if (messageKafka.getId() > 0) {
            Message message = messagesRepository.findById(messageKafka.getId()).orElseThrow();
            message.setReadStatus(ReadStatusTypes.READ);
            messagesRepository.save(message);
        } else {
            messagesRepository.save(messageKafka.getTime(), messageKafka.getMessageText(), messageKafka.getReadStatus().toString(),
                    messageKafka.getIsDeleted(), messageKafka.getAuthorId(), messageKafka.getRecipientId(), messageKafka.getDialogId());
        }
    }
}
