package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.mappers.NotificationMapper;
import soialNetworkApp.model.entities.Notification;

@RequiredArgsConstructor
@Service
public class NotificationsKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsKafkaProducer.class);
    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;
    private final NotificationMapper notificationMapper;

    public void sendMessage (NotificationKafka notificationKafka) {
        LOGGER.info(String.format("Sent -> %s", notificationKafka.toString()));

        kafkaTemplate.send("notifications", notificationKafka);
    }
}
