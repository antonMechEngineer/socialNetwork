package socialnet.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import socialnet.api.request.CommentRq;
import socialnet.api.response.PersonRs;
import socialnet.kafka.NotificationsKafkaProducer;
import socialnet.mappers.PersonMapper;
import socialnet.model.entities.Comment;
import socialnet.model.entities.Person;
import socialnet.model.entities.PersonSettings;
import socialnet.model.entities.Post;
import socialnet.repository.CommentsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class CommentsServiceTest {

    @Autowired
    private CommentsService commentsService;

    @MockBean
    private CommentsRepository commentsRepository;

    @MockBean
    private PersonsService personsService;

    @MockBean
    private PersonCacheService personCacheService;

    @MockBean
    private LikesService likesService;

    @MockBean
    private NotificationsKafkaProducer notificationsKafkaProducer;

    @MockBean
    private PersonMapper personMapper;

    private CommentRq request;
    private final String commentText = "comment text";
    private final String newText = "new comment text";
    private Person person;
    private Post post;
    private Comment comment;
    private PersonSettings personSettings;
    private final int offset = 0;
    private final int size = 20;

    @BeforeEach
    void setUp() {
        request = new CommentRq();
        request.setParentId(1L);
        request.setCommentText(commentText);
        person = new Person();
        person.setId(1L);
        post = new Post();
        post.setId(1L);
        post.setAuthor(person);
        comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(person);
        comment.setId(1L);
        comment.setCommentText(commentText);
        comment.setTime(LocalDateTime.now());
        personSettings = new PersonSettings();
        personSettings.setPostCommentNotification(true);
        personSettings.setCommentCommentNotification(true);
        person.setPersonSettings(personSettings);
    }

    @AfterEach
    void tearDown() {
        request = null;
        person = null;
        post = null;
        comment = null;
        personSettings = null;
    }

    @Test
    void createComment() {
        when(personCacheService.getPersonByContext()).thenReturn(person);
        when(commentsRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(true);
        when(personMapper.toPersonResponse(any())).thenReturn(PersonRs.builder().id(1L).build());

        assertEquals(commentsService.createComment(post, request).getData().getCommentText(), commentText);
        verify(notificationsKafkaProducer).sendMessage(any(), any());
    }

    @Test
    void getPostComments() {
        when(commentsRepository.findCommentsByPostOrderByTimeAsc(any(), any())).thenReturn(new PageImpl<>(List.of(comment)));
        when(personMapper.toPersonResponse(any())).thenReturn(PersonRs.builder().id(1L).build());

        assertEquals(commentsService.getPostComments(post, offset, size).getData().get(0).getCommentText(), commentText);
    }

    @Test
    void editComment() {
        request.setCommentText(newText);
        when(commentsRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(personsService.validatePerson(any())).thenReturn(true);
        when(commentsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(true);
        when(personMapper.toPersonResponse(any())).thenReturn(PersonRs.builder().id(1L).build());

        assertEquals(commentsService.editComment(anyLong(), request).getData().getCommentText(), newText);
    }

    @Test
    void changeCommentDeleteStatus() {
        when(commentsRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(personsService.validatePerson(any())).thenReturn(true);
        when(commentsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(true);
        when(personMapper.toPersonResponse(any())).thenReturn(PersonRs.builder().id(1L).build());

        assertEquals(commentsService.changeCommentDeleteStatus(anyLong(), true).getData().getIsDeleted(), true);
    }

    @Test
    void getCommentById() {
        commentsService.getCommentById(anyLong());

        verify(commentsRepository).findById(anyLong());
    }

    @Test
    void embeddedCommentsToResponse() {
        Comment embeddedComment = new Comment();
        embeddedComment.setId(2L);
        embeddedComment.setParentComment(comment);
        embeddedComment.setAuthor(person);
        embeddedComment.setTime(LocalDateTime.now());
        comment.setEmbeddedComments(new ArrayList<>(List.of(embeddedComment)));
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(true);
        when(personMapper.toPersonResponse(any())).thenReturn(PersonRs.builder().id(1L).build());

        assertEquals(2L, (long) commentsService.embeddedCommentsToResponse(new ArrayList<>(List.of(comment))).get(0).getEmbeddedComments().get(0).getId());
    }
}