package soialNetworkApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.NotificationRs;
import soialNetworkApp.kafka.NotificationsKafkaProducer;
import soialNetworkApp.errors.NoSuchEntityException;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.mappers.NotificationMapper;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.model.enums.NotificationTypes;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.NotificationsRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.service.util.NetworkPageRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private final PersonsRepository personsRepository;
    private final FriendshipsRepository friendshipsRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate template;
    private final NotificationsKafkaProducer notificationsKafkaProducer;

    @Value("${socialNetwork.default.page}")
    private int offset;
    @Value("${socialNetwork.default.noteSize}")
    private int size;
    @Value("${socialNetwork.timezone}")
    private String timezone;

    public CommonRs<List<NotificationRs>> getAllNotificationsByPerson(int offset, int perPage) throws Exception {
        Person person = personsRepository.findPersonByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(new PersonNotFoundException("Person is not available"));
        return getAllNotificationsByPerson(offset, perPage, person);
    }

    private CommonRs<List<NotificationRs>> getAllNotificationsByPerson(int offset, int perPage, Person person) {
        Pageable pageable = NetworkPageRequest.of(offset, perPage);
        Page<Notification> notificationPage = notificationsRepository.findAllByPersonAndIsReadIsFalse(person, pageable);
        return CommonRs.<List<NotificationRs>>builder()
                .total(notificationPage.getTotalElements())
                .offset(offset)
                .itemPerPage(perPage)
                .data(notificationPage.getContent().stream()
                        .map(notificationMapper::toNotificationResponse).collect(Collectors.toList()))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public CommonRs<List<NotificationRs>> markNotificationStatusAsRead(Long notificationId, boolean readAll) throws Exception {
        Person person = personsRepository.findPersonByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(new PersonNotFoundException("Person is not available"));
        if (readAll) {
            notificationsRepository.findAllByPersonAndIsReadIsFalse(person).forEach(notification -> {
                notification.setIsRead(true);
                notificationsKafkaProducer.sendMessage(notification);
            });
        } else {
            Notification notification = notificationsRepository.findById(notificationId)
                    .orElseThrow(new NoSuchEntityException("Notification with id " + notificationId + "was not found"));
            notification.setIsRead(true);
            notificationsKafkaProducer.sendMessage(notification);
        }
        return getAllNotificationsByPerson(offset, size, person);
    }

    public void createNotification(Notificationed entity, Person person)  {
        Notification notification = new Notification();
        notification.setIsRead(false);
        notification.setPerson(person);
        notification.setNotificationType(entity.getNotificationType());
        notification.setEntity(entity);
        notification.setSentTime(LocalDateTime.now(ZoneId.of(timezone)));
        notificationsKafkaProducer.sendMessage(notification);
        template.convertAndSend(String.format("/user/%s/queue/notifications", person.getId()),
                getAllNotificationsByPerson(offset, size, person));
    }

    @Scheduled(cron = "${socialNetwork.scheduling.birthdays}", zone = "${socialNetwork.timezone}")
    public void birthdaysNotificator() {
        LocalDateTime currentDate = LocalDateTime.now(ZoneId.of(timezone));
        List<Person> personList = personsRepository.findPeopleByBirthDate(currentDate.getMonthValue(), currentDate.getDayOfMonth());
        personList.forEach(person -> friendshipsRepository.findFriendshipsByDstPerson(person).forEach(friendship -> {
            if (friendship.getFriendshipStatus().equals(FriendshipStatusTypes.FRIEND) &&
                    friendship.getSrcPerson().getPersonSettings() != null &&
                    friendship.getSrcPerson().getPersonSettings().getFriendBirthdayNotification()) {
                createNotification(person, friendship.getSrcPerson());
            }
        }));
    }

    public void deleteNotification(Notificationed entity) {
        notificationsRepository.findNotificationByEntity(entity.getNotificationType(), entity)
                .ifPresent(notificationsRepository::delete);
    }
}
