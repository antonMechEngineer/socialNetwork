package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.repository.NotificationsRepository;
import soialNetworkApp.service.NotificationsService;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsKafkaConsumer {

    @Value("${socialNetwork.timezone}")
    private String timezone;
    private final NotificationsRepository notificationsRepository;
    private final NotificationsService notificationsService;

    @KafkaListener(topics = "notifications", autoStartup = "${listen.auto.start:true}")
    public void consume(NotificationKafka notificationKafka) {
        log.info(String.format("Json received -> %s", notificationKafka.toString()));
        if (notificationKafka.getId() > 0) {
            System.out.println("notificationConsumerInvoked");
            Notification notification = notificationsRepository.findById(notificationKafka.getId()).orElseThrow();
            notification.setIsRead(true);
            notificationsRepository.save(notification);
            notificationsService.sendNotificationsToWs(notification.getPerson());
        } else {
            notificationsRepository.save(notificationKafka.getNotificationType().toString(), notificationKafka.getNotificationedId(),
                    notificationKafka.getIsRead(), LocalDateTime.now(ZoneId.of(timezone)), notificationKafka.getPersonId());
            notificationsService.sendNotificationsToWs(notificationKafka.getPersonId());
//            notificationsService.sendNotificationToTelegramBot(notificationKafka.getNotificationType(), notificationKafka.getPersonId());
        }
    }

}

