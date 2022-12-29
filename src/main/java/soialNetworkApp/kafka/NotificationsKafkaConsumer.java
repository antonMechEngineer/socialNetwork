package soialNetworkApp.kafka;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.NotificationTypes;
import soialNetworkApp.repository.NotificationsRepository;

@Service
@RequiredArgsConstructor
public class NotificationsKafkaConsumer {

    private final NotificationsRepository notificationsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsKafkaConsumer.class);

    @KafkaListener(topics = "notifications", groupId = "myGroup")
    public void consume(NotificationKafka notificationKafka) {
        LOGGER.info(String.format("Json message received -> %s", notificationKafka.toString()));

        NotificationTypes notificationTypes = NotificationTypes.valueOf(notificationKafka.getNotificationType().toString());
        notificationsRepository.save(notificationTypes.toString(), notificationKafka.getNotificationedId(),
                notificationKafka.getIsRead(), notificationKafka.getSentTime(), notificationKafka.getPersonId());
    }
}
