package soialNetworkApp.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.model.entities.Notification;

@Service
public class NotificationsKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsKafkaProducer.class);
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationsKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage (Notification notification) {
        LOGGER.info(String.format("Message sent -> %s", notification.toString()));
        NotificationKafka notificationKafka = new NotificationKafka(
                notification.getNotificationType(),
                notification.getSentTime(),
                notification.getEntity(),
                notification.getPerson(),
                notification.getIsRead());
        String notificationText = "";
        try {
            notificationText = objectMapper.writeValueAsString(notificationKafka);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send("notifications", notificationText);
    }
}
