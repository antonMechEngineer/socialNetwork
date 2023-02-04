package socialnet.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import socialnet.kafka.NotificationsKafkaProducer;
import socialnet.model.entities.Notification;
import socialnet.repository.CommentsRepository;
import socialnet.repository.NotificationsRepository;
import socialnet.repository.PersonsRepository;
import socialnet.repository.PostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(refresh = BEFORE_EACH_TEST_METHOD)
@Sql("/test-data.sql")
@AutoConfigureMockMvc
@WithUserDetails("rhoncus.nullam@yahoo.edu")
class NotificationsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private PersonsRepository personsRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @MockBean
    private NotificationsKafkaProducer notificationsKafkaProducer;

    Notification notification1;
    Notification notification2;

    @BeforeEach
    void setUp() {
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setPerson(personsRepository.findById(1L).get());
        notification1.setEntity(postsRepository.findById(1L).get());
        notification1.setIsRead(false);
        notification1.setSentTime(LocalDateTime.now());
        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setPerson(personsRepository.findById(1L).get());
        notification2.setEntity(commentsRepository.findById(5L).get());
        notification2.setIsRead(false);
        notification2.setSentTime(LocalDateTime.now());
        notificationsRepository.saveAll(List.of(notification1, notification2));
    }

    @AfterEach
    void tearDown() {
        notification1 = null;
        notification2 = null;
    }

    @Test
    void getNotifications() throws Exception {
        mockMvc.perform(get("/api/v1/notifications")
                        .param("offset", "0")
                        .param("perPage", "10"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void markAsReadAllNotifications() throws Exception {
        doAnswer(invocationOnMock -> {
            List<Notification> notifications = notificationsRepository.findAll();
            for (Notification notification : notifications) {
                notification.setIsRead(true);
                notificationsRepository.save(notification);
            }
            return null;
        }).when(notificationsKafkaProducer).sendMessage(any());
        mockMvc.perform(put("/api/v1/notifications")
                        .param("all", "true"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void markAsReadNotification() throws Exception {
        doAnswer(invocationOnMock -> {
            Notification notification = notificationsRepository.findById(1L).orElseThrow();
            notification.setIsRead(true);
            notificationsRepository.save(notification);
            return null;
        }).when(notificationsKafkaProducer).sendMessage(any());
        mockMvc.perform(put("/api/v1/notifications")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(2));

    }
}