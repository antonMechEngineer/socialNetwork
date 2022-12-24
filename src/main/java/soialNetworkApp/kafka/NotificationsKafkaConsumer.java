package soialNetworkApp.kafka;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.repository.NotificationsRepository;

@Service
@RequiredArgsConstructor
public class NotificationsKafkaConsumer {

    private final NotificationsRepository notificationsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsKafkaConsumer.class);


    @KafkaListener(topics = "notifications", groupId = "myGroup")
    public void consume(Notification notification){
        LOGGER.info(String.format("Json message received -> %s", notification.toString()));
        notificationsRepository.save(notification);
    }
}
