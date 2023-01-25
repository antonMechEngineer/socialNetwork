package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.NotificationRs;
import soialNetworkApp.errors.NoSuchEntityException;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.kafka.NotificationsKafkaProducer;
import soialNetworkApp.mappers.NotificationMapper;
import soialNetworkApp.mappers.PersonMapper;
import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.model.enums.NotificationTypes;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.NotificationsRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.service.util.NetworkPageRequest;

import java.io.IOException;
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
    private final PersonMapper personMapper;

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

    public void sendNotificationsToWs(Person person)  {
//        notificationsKafkaProducer.sendMessage(entity, person);

        template.convertAndSend(String.format("/user/%s/queue/notifications", person.getId()),
                getAllNotificationsByPerson(offset, size, person));
    }

    public void sendNotificationsToWs(long personId)  {
        sendNotificationsToWs(personsRepository.findPersonById(personId).orElseThrow());
    }

    @Scheduled(cron = "${socialNetwork.scheduling.birthdays}", zone = "${socialNetwork.timezone}")
    public void birthdaysNotificator() {
        LocalDateTime currentDate = LocalDateTime.now(ZoneId.of(timezone));
        List<Person> personList = personsRepository.findPeopleByBirthDate(currentDate.getMonthValue(), currentDate.getDayOfMonth());
        personList.forEach(person -> friendshipsRepository.findFriendshipsByDstPerson(person).forEach(friendship -> {
            if (friendship.getFriendshipStatus().equals(FriendshipStatusTypes.FRIEND) &&
                    friendship.getSrcPerson().getPersonSettings() != null &&
                    friendship.getSrcPerson().getPersonSettings().getFriendBirthdayNotification()) {
//                createNotification(person, friendship.getSrcPerson());
                notificationsKafkaProducer.sendMessage(person, friendship.getSrcPerson());
            }
        }));
    }

    public void deleteNotification(Notificationed entity) {
        notificationsRepository.findNotificationByEntity(entity.getNotificationType(), entity)
                .ifPresent(notificationsRepository::delete);
    }

    public void sendNotificationToTelegramBot(NotificationTypes notificationType, long personId) {
        Person person = personsRepository.findPersonById(personId).orElse(new Person());
        if (person.getTelegramId() != null) {
            HttpClient httpClient = HttpClientBuilder.create().build();
            try {
                HttpPost request = new HttpPost("http://194.87.244.66:8087/bot?userId=" + person.getTelegramId());
                JSONObject jsonObject = new JSONObject()
                        .put("notification_type", notificationType)
                        .put("entity_author", new JSONObject()
                                .put("first_name", person.getFirstName()).put("last_name", person.getLastName()));
                StringEntity params = new StringEntity(jsonObject.toString());
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                httpClient.execute(request);
            } catch (IOException ignore) {
            }
        }
    }
}
