package main.service;

import main.api.request.LikeRequest;
import main.errors.NoPostEntityException;
import main.model.entities.Person;
import main.model.entities.Post;
import main.model.entities.PostLike;
import main.repository.PostLikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikesService {

    private final PostLikesRepository likesRepository;
    private final PostsService postsService;

    @Autowired
    public LikesService(PostLikesRepository likesRepository, PostsService postsService) {
        this.likesRepository = likesRepository;
        this.postsService = postsService;
    }

    public PostLike putLike(LikeRequest likeRequest, Person person) throws NoPostEntityException {
        Post post = postsService.findPostById(likeRequest.getItemId());
        if (validateLikeFromCurrentPerson(person, post)) {
            return getLikeByPersonFromPost(person, post);
        }
        PostLike like = new PostLike();
        like.setPost(post);
        like.setPerson(person);
        like.setType(likeRequest.getType());
        like.setTime(LocalDateTime.now());
        return likesRepository.save(like);
    }

    public List<PostLike> getLikesByPost(Post post) {
        return likesRepository.findPostLikesByPost(post);
    }

    public boolean validateLikeFromCurrentPerson(Person person, Post post) {
        return getLikesByPost(post).stream().anyMatch(postLike -> postLike.getPerson().getId().equals(person.getId()));
    }

    public void deleteLike(Person person, Post post) {
        likesRepository.delete(getLikeByPersonFromPost(person, post));
    }

    private PostLike getLikeByPersonFromPost(Person person, Post post) {
        return post.getPostLikes().stream().filter(postLike -> postLike.getPerson().getId().equals(person.getId())).findFirst().get();
    }
}
