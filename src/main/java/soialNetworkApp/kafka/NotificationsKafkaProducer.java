package soialNetworkApp.kafka;

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
    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;

    public NotificationsKafkaProducer(KafkaTemplate<String, NotificationKafka> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage (Notification notification) {
        LOGGER.info(String.format("Sent -> %s", notification.toString()));

        NotificationKafka notificationKafka = new NotificationKafka(
                notification.getNotificationType(),
                notification.getSentTime(),
                notification.getEntity().getId(),
                notification.getPerson().getId(),
                notification.getIsRead());
        kafkaTemplate.send("notifications", notificationKafka);
    }
}
