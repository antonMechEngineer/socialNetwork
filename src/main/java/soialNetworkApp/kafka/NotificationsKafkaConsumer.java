package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.repository.NotificationsRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsKafkaConsumer {
    private final NotificationsRepository notificationsRepository;

    @KafkaListener(topics = "notifications", groupId = "myGroup", autoStartup = "${listen.auto.start:true}")
    public void consume(NotificationKafka notificationKafka) {
        log.info(String.format("Json received -> %s", notificationKafka.toString()));
        if (notificationKafka.getId() > 0) {
            System.out.println("notificationConsumerInvoked");
            Notification notification = notificationsRepository.findById(notificationKafka.getId()).orElseThrow();
            notification.setIsRead(true);
            notificationsRepository.save(notification);
        } else {
            notificationsRepository.save(notificationKafka.getNotificationType().toString(), notificationKafka.getNotificationedId(),
                    notificationKafka.getIsRead(), LocalDateTime.now(), notificationKafka.getPersonId());
        }
    }

}

