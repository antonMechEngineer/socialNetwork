package main.service;

import main.api.request.PostRequest;
import main.errors.NoPostEntityException;
import main.model.entities.Post;
import main.model.entities.User;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Autowired
    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public Post createPost(PostRequest postRequest) {
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

    public Page<Post> getAllPostsByAuthor(int page, int size, User postsAuthor) {
        Pageable pageable = PageRequest.of(page, size);
        return postsRepository.findPostsByAuthorIDOrderByTimeDesc(pageable, postsAuthor.getId());
    }

    public Post findPostById(long postId) throws NoPostEntityException {
        return postsRepository.findById(postId).orElseThrow(() -> new NoPostEntityException("Post with id " + postId + " does not exist"));
    }

    public Post updatePost(long postId, PostRequest postRequest) throws NoPostEntityException {
        Post post = findPostById(postId);
        post.setTitle(postRequest.getTitle());
        post.setPostText(postRequest.getPostText());
        return postsRepository.save(post);
    }

    public void deletePost(long postId) {
        postsRepository.deleteById(postId);
    }
}
