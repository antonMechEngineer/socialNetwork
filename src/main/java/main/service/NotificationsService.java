package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.CommonResponse;
import main.api.response.NotificationResponse;
import main.mappers.NotificationMapper;
import main.model.entities.Notification;
import main.model.entities.Person;
import main.model.entities.interfaces.Notificationed;
import main.repository.NotificationsRepository;
import main.repository.PersonsRepository;
import main.service.util.NetworkPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private final PersonsRepository personsRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate template;

    public CommonResponse<List<NotificationResponse>> getAllNotificationsByPerson(int offset, int perPage) {
        Person person = personsRepository.findPersonByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        Pageable pageable = NetworkPageRequest.of(offset, perPage);
        Page<Notification> notificationPage = notificationsRepository.findAllByPersonAndIsReadIsFalse(person, pageable);
        return CommonResponse.<List<NotificationResponse>>builder()
                .total(notificationPage.getTotalElements())
                .offset(offset)
                .itemPerPage(perPage)
                .data(notificationPage.getContent().stream()
                        .map(notificationMapper::toNotificationResponse).collect(Collectors.toList()))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public CommonResponse<List<NotificationResponse>> markNotificationStatusAsRead(Long notificationId, boolean readAll) {
        Person person = personsRepository.findPersonByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if (readAll) {
            notificationsRepository.findAllByPersonAndIsReadIsFalse(person).forEach(notification -> {
                notification.setIsRead(true);
                notificationsRepository.save(notification);
            });
        } else {
            Notification notification = notificationsRepository.findById(notificationId).get();
            notification.setIsRead(true);
            notificationsRepository.save(notification);
        }
        return getAllNotificationsByPerson(0, 10);
    }

    public void createNotification(Notificationed entity, Person person) {
        Notification notification = new Notification();
        notification.setIsRead(false);
        notification.setPerson(person);
        notification.setEntity(entity);
        notification.setSentTime(LocalDateTime.now());
        sendNotification(notificationsRepository.save(notification));
    }

    private void sendNotification(Notification notification) {
        NotificationResponse response = notificationMapper.toNotificationResponse(notification);
        template.convertAndSend(String.format("/user/%s/queue/notifications", notification.getPerson().getId()),
                Map.of("body", Map.of("data", response)));
    }
}
