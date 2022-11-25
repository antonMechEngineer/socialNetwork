package main.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import main.model.entities.Person;
import main.model.entities.Post;
import main.model.entities.Tag;
import main.repository.TagsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class TagsServiceTest {

    @Autowired
    private TagsService tagsService;

    @MockBean
    private TagsRepository tagsRepository;

    private Tag tag;
    private Post post;

    @BeforeEach
    void setUp() {
        post =new Post();
        post.setAuthor(new Person());
        tag = new Tag("tag1");

    }

    @AfterEach
    void tearDown() {
        tag = null;
        post = null;
    }

    @Test
    void stringsToTagsMapper() {
        when(tagsRepository.findByTagName(any())).thenReturn(null);
        when(tagsRepository.save(any())).thenReturn(tag);
        assertEquals(tagsService.stringsToTagsMapper(null).size(), 0);
        assertTrue(tagsService.stringsToTagsMapper(List.of(anyString())).contains(tag));
    }

    @Test
    void tagsToStringsMapper() {
        assertEquals(tagsService.tagsToStringsMapper(null).size(), 0);
        assertTrue(tagsService.tagsToStringsMapper(List.of(tag)).contains(tag.getTagName()));
    }

    @Test
    void addPostToTag() {
        assertTrue(tagsService.addPostToTag(tag, post).getPosts().contains(post));
        tag.setPosts(List.of(post));
        assertTrue(tagsService.addPostToTag(tag, post).getPosts().contains(post));

    }

    @Test
    void dropPostFromTag() {
        tag.setPosts(List.of(post));
        when(tagsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        assertTrue(tagsService.dropPostFromTag(tag, post).getPosts().isEmpty());
    }
}