package socialnet.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import socialnet.kafka.dto.NotificationKafka;
import socialnet.mappers.NotificationMapper;
import socialnet.model.entities.Notification;
import socialnet.model.entities.Person;
import socialnet.model.entities.interfaces.Notificationed;
import socialnet.model.enums.NotificationTypes;

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

    public void sendMessage(Notificationed notificationed, Person person) {
        NotificationKafka notificationKafka =
                notificationMapper.toNotificationKafkaFromNotificationed(notificationed, person);
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
