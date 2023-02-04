
//https://gitlab.skillbox.ru/javapro_team30/social-telegram-bot.git
package socialnet.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import socialnet.kafka.dto.NotificationKafka;
import socialnet.model.entities.Notification;
import socialnet.repository.NotificationsRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsKafkaConsumer {

    @Value("${socialNetwork.timezone}")
    private String timezone;
    private final NotificationsRepository notificationsRepository;

    @KafkaListener(topics = "notifications", autoStartup = "${listen.auto.start:true}")
    public void consume(NotificationKafka notificationKafka) {
        log.info(String.format("Json received -> %s", notificationKafka.toString()));
        if (notificationKafka.getId() > 0) {
            System.out.println("notificationConsumerInvoked");
            Notification notification = notificationsRepository.findById(notificationKafka.getId()).orElseThrow();
            notification.setIsRead(true);
            notificationsRepository.save(notification);
        } else {
            notificationsRepository.save(notificationKafka.getNotificationType().toString(), notificationKafka.getNotificationedId(),
                    notificationKafka.getIsRead(), LocalDateTime.now(ZoneId.of(timezone)), notificationKafka.getPersonId());
        }
    }

}

