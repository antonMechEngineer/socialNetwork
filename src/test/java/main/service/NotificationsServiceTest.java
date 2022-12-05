package main.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.response.PersonResponse;
import main.mappers.PersonMapper;
import main.model.entities.Notification;
import main.model.entities.Person;
import main.model.entities.Post;
import main.model.enums.NotificationTypes;
import main.repository.NotificationsRepository;
import main.repository.PersonsRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private PersonMapper personMapper;
    @MockBean
    private SimpMessagingTemplate template;

    private Person person;
    private Notification notification1;
    private Notification notification2;
    private Post post;
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
    }

    @AfterEach
    void tearDown() {
        person = null;
        notification1 = null;
        notification2 = null;
        post = null;
    }

    @Test
    void getAllNotificationsByPerson() {
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(List.of(notification1)));
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());

        assertEquals(notificationsService.getAllNotificationsByPerson(offset, size).getData().stream().findFirst().get().getNotificationType(), NotificationTypes.POST);
        assertEquals(notificationsService.getAllNotificationsByPerson(offset, size).getData().stream().findFirst().get().getEntityAuthor().getId(), 1L);
    }

    @Test
    void markAllNotificationsStatusAsRead() {
        List<Notification> notifications = new ArrayList<>();
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(List.of(notification1, notification2)));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any())).thenReturn(List.of(notification1, notification2));
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());
        when(notificationsRepository.save(any())).then(invocation -> {
            Notification notification = invocation.getArgument(0);
            notifications.add(notification);
            return notification;
        });

        notificationsService.markNotificationStatusAsRead(null, true);
        verify(notificationsRepository, Mockito.times(2)).save(any());
        assertEquals(2, notifications.size());
        assertTrue(notifications.stream().map(Notification::getIsRead).allMatch(aBoolean -> aBoolean.equals(true)));
    }

    @Test
    void markNotificationStatusAsRead() {
        List<Notification> notifications = new ArrayList<>();
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(List.of(notification1, notification2)));
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any())).thenReturn(List.of(notification1, notification2));
        when(notificationsRepository.findById(any())).thenReturn(Optional.of(notification1));
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());
        when(notificationsRepository.save(any())).then(invocation -> {
            Notification notification = invocation.getArgument(0);
            notifications.add(notification);
            return notification;
        });

        notificationsService.markNotificationStatusAsRead(1L, false);
        verify(notificationsRepository).save(any());
        assertEquals(1, notifications.size());
        assertTrue(notifications.get(0).getIsRead());
    }

    @Test
    void createNotification() {
        when(notificationsRepository.findAllByPersonAndIsReadIsFalse(any(), any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        notificationsService.createNotification(post, person);
        verify(notificationsRepository).save(any());
        verify(template).convertAndSend(anyString(), (Object) any());
    }
}