package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.mappers.NotificationMapper;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.NotificationTypes;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationsKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsKafkaProducer.class);
    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;
    private final NotificationMapper notificationMapper;

    public void sendMessage(NotificationTypes notificationType, Long notificationId, Person person) {
        NotificationKafka notificationKafka =
                notificationMapper.toNotificationKafkaFromNotificationed(notificationType, notificationId, person);
        sendMessageKafka(notificationKafka);
    }

    public void sendMessage(Notification notification) {
        System.out.println("notificationProdInvoked");
        NotificationKafka notificationKafka = notificationMapper.toNotificationKafka(notification);
        sendMessageKafka(notificationKafka);
    }

    private void sendMessageKafka(NotificationKafka notificationKafka){
        log.info(String.format("Sent -> %s", notificationKafka.toString()));
        kafkaTemplate.send("notifications", notificationKafka);
    }

}
