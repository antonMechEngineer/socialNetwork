package main.service;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.errors.NoPostEntityException;
import main.model.entities.Person;
import main.model.entities.Post;
import main.model.entities.Tag;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final TagsService tagsService;

    @Autowired
    public PostsService(PostsRepository postsRepository, TagsService tagsService) {
        this.postsRepository = postsRepository;
        this.tagsService = tagsService;
    }

    public Post createPost(PostRequest postRequest) {
        Logger.getLogger(this.getClass().getName()).info("createPost with title " + postRequest.getTitle() + " and text " + postRequest.getPostText());
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setPostText(postRequest.getPostText());
        return postsRepository.save(post);
    }

    public Page<Post> getAllPosts(int page, int size) {
        Logger.getLogger(this.getClass().getName()).info("getAllPosts with page " + page + " and size " + size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        return postsRepository.findAll(pageable);
    }

    public Page<Post> getAllPostsByAuthor(int page, int size, Person postsAuthor) {
        Logger.getLogger(this.getClass().getName()).info("getAllPostsByAuthor by " + postsAuthor + " with page " + page + " and size " + size);
        Pageable pageable = PageRequest.of(page, size);
        return postsRepository.findPostsByAuthorOrderByTimeDesc(pageable, postsAuthor);
    }

    public Post findPostById(long postId) throws NoPostEntityException {
        Logger.getLogger(this.getClass().getName()).info("findPostById with postId " + postId);
        return postsRepository.findById(postId).orElseThrow(() -> new NoPostEntityException("Post with id " + postId + " does not exist"));
    }

    public Post updatePost(long postId, PostRequest postRequest) throws NoPostEntityException {
        Logger.getLogger(this.getClass().getName()).info("updatePost with title " + postRequest.getTitle() + " and text " + postRequest.getPostText());
        Post post = findPostById(postId);
        post.setTitle(postRequest.getTitle());
        post.setPostText(postRequest.getPostText());
        return postsRepository.save(post);
    }

    public Post deletePost(long postId) throws NoPostEntityException {
        Logger.getLogger(this.getClass().getName()).info("deletePost with postId " + postId);
        Post post = findPostById(postId);
        post.setIsDeleted(true);
        post.setTimeDelete(LocalDateTime.now());
        return postsRepository.save(post);
    }

    public Post addTagToPost(long postId, String tagName) throws NoPostEntityException {
        Post post = findPostById(postId);
        Tag tag = tagsService.getTagByTagName(tagName);
        List<Tag> tagList = new ArrayList<>(post.getTags());
        tagList.add(tag);
        post.setTags(tagList);
        return postsRepository.save(post);
    }

    public Post dropTagFromPost(long postId, String tagName) throws NoPostEntityException {
        Post post = findPostById(postId);
        Tag tag = tagsService.getTagByTagName(tagName);
        List<Tag> tagList = new ArrayList<>(post.getTags());
        tagList.remove(tag);
        post.setTags(tagList);
        return postsRepository.save(post);
    }

    public List<PostResponse> postsToResponse(List<Post> posts) {
        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }
}
