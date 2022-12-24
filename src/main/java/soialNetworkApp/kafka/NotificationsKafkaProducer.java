package soialNetworkApp.kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import soialNetworkApp.model.entities.Notification;

@Service

public class NotificationsKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsKafkaProducer.class);
    private KafkaTemplate<String, Notification> kafkaTemplate;

    public NotificationsKafkaProducer(KafkaTemplate<String, Notification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage (Notification data){
        LOGGER.info(String.format("Message sent -> %s", data.toString()));
        Message<Notification> message = MessageBuilder.withPayload(data).
                setHeader(KafkaHeaders.TOPIC, "notifications").build();
        kafkaTemplate.send(message);
    }
}
