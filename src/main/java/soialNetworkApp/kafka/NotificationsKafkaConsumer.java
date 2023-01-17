package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.repository.NotificationsRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsKafkaConsumer {
    private final NotificationsRepository notificationsRepository;

    @KafkaListener(topics = "notifications", groupId = "myGroup")
    public void consume(NotificationKafka notificationKafka) {
        log.info(String.format("Json received -> %s", notificationKafka.toString()));
        if (notificationKafka.getId() > 0) {
            Notification notification = notificationsRepository.findById(notificationKafka.getId()).orElseThrow();
            notification.setIsRead(true);
            notificationsRepository.save(notification);
        } else {
            notificationsRepository.save(notificationKafka.getNotificationType(), notificationKafka.getNotificationedId(),
                    notificationKafka.getIsRead(), notificationKafka.getSentTime(), notificationKafka.getPersonId());
        }
    }

}

