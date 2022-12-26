package soialNetworkApp.kafka;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.repository.NotificationsRepository;

@Service
@RequiredArgsConstructor
public class NotificationsKafkaConsumer {

    private final NotificationsRepository notificationsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsKafkaConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "notifications", groupId = "myGroup")
    public void consume(ConsumerRecord<String, String> consumerRecord) {
        LOGGER.info(String.format("Json message received -> %s", consumerRecord));

        NotificationKafka notificationKafka = null;
        try {
            notificationKafka = objectMapper.readValue(consumerRecord.value(), NotificationKafka.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Notification notification = new Notification();
        notification.setNotificationType(notificationKafka.getNotificationType());
        notification.setSentTime(notificationKafka.getSentTime());
        notification.setEntity(notificationKafka.getEntity());
        notification.setPerson(notificationKafka.getPerson());
        notification.setIsRead(notificationKafka.getIsRead());
        notificationsRepository.save(notification);
    }
}
