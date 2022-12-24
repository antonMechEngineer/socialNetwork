package soialNetworkApp.kafka;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.repository.NotificationsRepository;

@Service
@RequiredArgsConstructor
public class MessagesKafkaConsumer {

    private final MessagesRepository messagesRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesKafkaConsumer.class);


    @KafkaListener(topics = "messages", groupId = "myGroup")
    public void consume(Object object){
        LOGGER.info(String.format("Json message received -> %s", object.toString()));
        //messagesRepository.save(message);
    }
}
