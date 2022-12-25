package soialNetworkApp.kafka;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "notifications", groupId = "myGroup")
    public void consume(ConsumerRecord<String, String> consumerRecord) {
        LOGGER.info(String.format("Json message received -> %s", consumerRecord));
        Notification notification = null;
        try {
            notification = objectMapper.readValue(consumerRecord.value(), Notification.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        notificationsRepository.save(notification);
    }
}
