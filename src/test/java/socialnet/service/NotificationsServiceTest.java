package socialnet.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import socialnet.api.response.PersonRs;
import socialnet.kafka.NotificationsKafkaProducer;
import socialnet.mappers.PersonMapper;
import socialnet.model.entities.*;
import socialnet.model.enums.FriendshipStatusTypes;
import socialnet.model.enums.NotificationTypes;
import socialnet.repository.FriendshipsRepository;
import socialnet.repository.NotificationsRepository;
import socialnet.repository.PersonsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class NotificationsServiceTest {

    @Autowired
    private NotificationsService notificationsService;

    @MockBean
    private Authentication authentication;
    @MockBean
    private PersonsRepository personsRepository;
    @MockBean
    private NotificationsRepository notificationsRepository;
    @MockBean
    private FriendshipsRepository friendshipsRepository;
    @MockBean
    private PersonMapper personMapper;
    @MockBean
    private SimpMessagingTemplate template;
    @MockBean
    private NotificationsKafkaProducer notificationsKafkaProducer;


    private Person person;
    private Notification notification1;
    private Notification notification2;
    private Post post;
    private PersonSettings personSettings;
    private Friendship friendship;
    private final int offset = 0;
    private final int size = 10;

    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        person = new Person();
        person.setId(1L);
        post = new Post();
        post.setAuthor(person);
        notification1 = new Notification();
        notification2 = new Notification();
        notification1.setId(1L);
        notification1.setId(2L);
        notification1.setEntity(post);
        notification2.setEntity(post);
        notification1.setIsRead(false);
        notification2.setIsRead(false);
        notification1.setPerson(person);
        notification2.setPerson(person);
        personSettings = new PersonSettings();
        personSettings.setFriendBirthdayNotification(true);
        person.setPersonSettings(personSettings);
        friendship = new Friendship();
        friendship.setSrcPerson(person);
        friendship.setDstPerson(person);
        friendship.setFriendshipStatus(FriendshipStatusTypes.FRIEND);
    }

    @AfterEach
    void tearDown() {
        person = null;
        notification1 = null;
        notification2 = null;
        post = null;
        personSettings = null;
        friendship = null;
    }

    @Test
    void getAllNotificationsByPerson() throws Exception {
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(List.of(notification1)));
        when(personMapper.toPersonResponse(any())).thenReturn(PersonRs.builder().id(1L).build());

        assertEquals(notificationsService.getAllNotificationsByPerson(offset, size).getData().stream().findFirst().get().getNotificationType(), NotificationTypes.POST);
        assertEquals(notificationsService.getAllNotificationsByPerson(offset, size).getData().stream().findFirst().get().getEntityAuthor().getId(), 1L);
    }
//такой функциональности в отдельном методе нет в сервисе, она внутри mark... под условием. М.б. лучше проверить в отдельном методе, что бы не было дублирований
    @Test
    void markAllNotificationsStatusAsRead() throws Exception {
//      List<Notification> notifications = new ArrayList<>();
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(List.of(notification1, notification2)));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any())).thenReturn(List.of(notification1, notification2));
        when(personMapper.toPersonResponse(any())).thenReturn(PersonRs.builder().id(1L).build());

        //наверное достаточно проверить что был вызов send/save
//        when(notificationsRepository.save(any())).then(invocation -> {
//            Notification notification = invocation.getArgument(0);
//            notifications.add(notification);
//            return notification;
//        });
        notificationsService.markNotificationStatusAsRead(null, true);
        verify(notificationsKafkaProducer, Mockito.times(2)).sendMessage(any());//verify(notificationsRepository, Mockito.times(2)).save(any());
//        assertEquals(2, notifications.size());
//        assertTrue(notifications.stream().map(Notification::getIsRead).allMatch(aBoolean -> aBoolean.equals(true)));
        assertTrue(notification1.getIsRead());
        assertTrue(notification2.getIsRead());

    }

    @Test
    void markNotificationStatusAsRead() throws Exception {
//      List<Notification> notifications = new ArrayList<>();
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(List.of(notification1, notification2)));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any())).thenReturn(List.of(notification1, notification2));
        when(notificationsRepository.findById(any())).thenReturn(Optional.of(notification1));
        when(personMapper.toPersonResponse(any())).thenReturn(PersonRs.builder().id(1L).build());

//наверное достаточно проверить что был вызов send/save
//        when(notificationsRepository.save(any())).then(invocation -> {
//            Notification notification = invocation.getArgument(0);
//            notifications.add(notification);
//            return notification;
//        });

        notificationsService.markNotificationStatusAsRead(1L, false);
        verify(notificationsKafkaProducer).sendMessage(any()); //verify(notificationsRepository).save(any());
//        assertEquals(1, notifications.size());
        assertTrue(notification1.getIsRead()); //assertTrue(notifications.get(0).getIsRead());
    }

    @Test
    void sendNotificationsToWs() {
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        notificationsService.sendNotificationToWs(post, person);
//        verify(notificationsKafkaProducer).sendMessage(any(), any(), any());    //verify(notificationsRepository).save(any());
        verify(template).convertAndSend(anyString(), (Object) any());
    }

    @Test
    void birthdayNotificator() {
        when(personsRepository.findPeopleByBirthDate(anyInt(), anyInt())).thenReturn(List.of(person));
        when(friendshipsRepository.findFriendshipsByDstPerson(any())).thenReturn(List.of(friendship));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        notificationsService.birthdaysNotificator();
        verify(notificationsKafkaProducer).sendMessage(any(), any()); //verify(notificationsRepository).save(any());
    }

    @Test
    void deleteNotification() {
        when(notificationsRepository.findNotificationByEntity(any(), any())).thenReturn(Optional.of(new Notification()));

        notificationsService.deleteNotification(post);
        verify(notificationsRepository).delete(any());
    }
}