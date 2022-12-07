package main.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.api.request.PostRequest;
import main.api.response.PersonResponse;
import main.errors.PersonNotFoundException;
import main.mappers.PersonMapper;
import main.model.entities.Friendship;
import main.model.entities.FriendshipStatus;
import main.model.entities.Person;
import main.model.entities.Post;
import main.model.enums.FriendshipStatusTypes;
import main.repository.FriendshipsRepository;
import main.repository.PersonsRepository;
import main.repository.PostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class PostsServiceTest {

    @Autowired
    private PostsService postsService;

    @MockBean
    private PostsRepository postsRepository;
    @MockBean
    private PersonsRepository personsRepository;
    @MockBean
    private FriendshipsRepository friendshipsRepository;
    @MockBean
    private TagsService tagsService;
    @MockBean
    private LikesService likesService;
    @MockBean
    private CommentsService commentsService;
    @MockBean
    private NotificationsService notificationsService;
    @MockBean
    private PersonMapper personMapper;
    @MockBean
    private Authentication authentication;

    private PostRequest postRequest;
    private Person person;
    private Post post;
    private FriendshipStatus friendshipStatus;
    private Friendship friendship;
    private final String postTitle = "postTitle";
    private final String postText = "postText";
    private final int offset = 0;
    private final int size = 20;

    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        friendshipStatus = new FriendshipStatus();
        friendshipStatus.setTime(LocalDateTime.now());
        friendshipStatus.setCode(FriendshipStatusTypes.FRIEND);
        friendship = new Friendship();
        friendship.setFriendshipStatus(friendshipStatus);
        friendship.setSentTime(LocalDateTime.now());
        friendship.setSrcPerson(new Person());
        friendship.setDstPerson(person);
        postRequest = new PostRequest();
        postRequest.setTitle(postTitle);
        postRequest.setPostText(postText);
        person = new Person();
        person.setId(1L);
        post = new Post();
        post.setAuthor(person);
        post.setPostText(postText);
        post.setTitle(postTitle);
        post.setIsDeleted(false);
        post.setIsBlocked(false);
        post.setTime(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        postRequest = null;
        person = null;
        post = null;
        friendshipStatus = null;
        friendship = null;
    }

    @Test
    void createPost() throws PersonNotFoundException {
        when(personsRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(tagsService.tagsToStringsMapper(any())).thenReturn(new ArrayList<>());
        when(tagsService.stringsToTagsMapper(any())).thenReturn(new ArrayList<>());
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(false);
        when(commentsService.embeddedCommentsToResponse(any())).thenReturn(new ArrayList<>());
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());
        when(postsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(friendshipsRepository.findFriendshipsByDstPerson(any())).thenReturn(List.of(friendship));

        assertEquals(postText, postsService.createPost(postRequest, 1L, null).getData().getPostText());
        verify(notificationsService).createNotification(any(), any());
    }

    @Test
    void getFeeds() {
        when(postsRepository.findPostsByTimeBeforeAndIsDeletedFalseOrderByTimeDesc(any(), any())).thenReturn(new PageImpl<>(List.of(post)));
        when(tagsService.tagsToStringsMapper(any())).thenReturn(new ArrayList<>());
        when(tagsService.stringsToTagsMapper(any())).thenReturn(new ArrayList<>());
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(false);
        when(commentsService.embeddedCommentsToResponse(any())).thenReturn(new ArrayList<>());
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());

        assertEquals(postText, postsService.getFeeds(offset, size).getData().get(0).getPostText());
    }

    @Test
    void getAllPostsByAuthor() {
        when(postsRepository.findPostsByAuthorOrderByTimeDesc(any(), any())).thenReturn(new PageImpl<>(List.of(post)));
        when(tagsService.tagsToStringsMapper(any())).thenReturn(new ArrayList<>());
        when(tagsService.stringsToTagsMapper(any())).thenReturn(new ArrayList<>());
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(false);
        when(commentsService.embeddedCommentsToResponse(any())).thenReturn(new ArrayList<>());
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());

        assertEquals(postText, postsService.getAllPostsByAuthor(offset, size, person).getData().get(0).getPostText());
    }

    @Test
    void getPostById() {
        when(postsRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(tagsService.tagsToStringsMapper(any())).thenReturn(new ArrayList<>());
        when(tagsService.stringsToTagsMapper(any())).thenReturn(new ArrayList<>());
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(false);
        when(commentsService.embeddedCommentsToResponse(any())).thenReturn(new ArrayList<>());
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());

        assertEquals(postText, postsService.getPostById(anyLong()).getData().getPostText());
    }

    @Test
    void updatePost() throws PersonNotFoundException {
        post.setPostText("errorText");
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(postsRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(tagsService.tagsToStringsMapper(any())).thenReturn(new ArrayList<>());
        when(tagsService.stringsToTagsMapper(any())).thenReturn(new ArrayList<>());
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(false);
        when(commentsService.embeddedCommentsToResponse(any())).thenReturn(new ArrayList<>());
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());
        when(postsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(postText, postsService.updatePost(anyLong(), postRequest).getData().getPostText());
    }

    @Test
    void changeDeleteStatusInPost() throws PersonNotFoundException {
        AtomicReference<Post> postToDelete = new AtomicReference<>();
        when(personsRepository.findPersonByEmail(any())).thenReturn(Optional.of(person));
        when(postsRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(tagsService.tagsToStringsMapper(any())).thenReturn(new ArrayList<>());
        when(tagsService.stringsToTagsMapper(any())).thenReturn(new ArrayList<>());
        when(likesService.getLikesCount(any())).thenReturn(0);
        when(likesService.getMyLike(any())).thenReturn(false);
        when(commentsService.embeddedCommentsToResponse(any())).thenReturn(new ArrayList<>());
        when(personMapper.toPersonResponse(any())).thenReturn(PersonResponse.builder().id(1L).build());
        when(postsRepository.save(any())).then(invocation -> {
            Post savedPost = invocation.getArgument(0);
            postToDelete.set(savedPost);
            return savedPost;
        });

        postsService.changeDeleteStatusInPost(anyLong(), true);
        assertTrue(postToDelete.get().getIsDeleted());
    }

    @Test
    void findPostById() {
        postsService.getPostById(anyLong());

        verify(postsRepository).findById(anyLong());
    }
}