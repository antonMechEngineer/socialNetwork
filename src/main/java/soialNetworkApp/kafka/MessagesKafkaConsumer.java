package soialNetworkApp.kafka;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.repository.MessagesRepository;


@Service
@RequiredArgsConstructor
public class MessagesKafkaConsumer {

    // TODO: 28.12.2022 опасные места: не запутается ли кафка тэплэйт из за того что где- то сообщения приходят, а где-то нотификации
    // TODO: 28.12.2022 не будет ли проблем, что у меня и messagesConsumer и  notificationConsumer ссылаются на myGroup
    private final MessagesRepository messagesRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesKafkaConsumer.class);

    @KafkaListener(topics = "messages", groupId = "myGroup")
    public void consume(MessageKafka messageKafka){
        LOGGER.info(String.format("Json message received -> %s", messageKafka));
        messagesRepository.save(messageKafka.getTime(), messageKafka.getMessageText(), messageKafka.getReadStatus().toString(),
                messageKafka.getIsDeleted(), messageKafka.getAuthorId(), messageKafka.getRecipientId(), messageKafka.getDialogId());

    }
}
