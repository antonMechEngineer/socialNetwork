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
import soialNetworkApp.model.entities.Notification;

@Service
public class NotificationsKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsKafkaProducer.class);
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationsKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage (Notification data) {
        String message = null;
        try {
            message = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!" + message);
        LOGGER.info(String.format("Message sent -> %s", message));
        kafkaTemplate.send("notifications", message);
    }
}
