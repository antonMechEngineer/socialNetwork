package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.LikeRequest;
import main.api.response.CommonResponse;
import main.api.response.LikeResponse;
import main.model.entities.Like;
import main.model.entities.Liked;
import main.model.entities.Person;
import main.model.enums.LikeTypes;
import main.repository.LikesRepository;
import org.mapstruct.Named;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostsService postsService;
    private final CommentsService commentsService;
    private final PersonsService personsService;

    public CommonResponse<LikeResponse> putLike(LikeRequest likeRequest) {
        Person person = personsService.getPersonByEmail((SecurityContextHolder.getContext().getAuthentication().getName()));
        Liked liked;
        switch (likeRequest.getType()) {
            case "Post" : {
                liked = postsService.findPostById(likeRequest.getItemId());
                break;
            }
            case "Comment" : {
                liked = commentsService.getCommentById(likeRequest.getItemId());
                break;
            }
            default: liked = null;
        }
        assert liked != null;
        if (validateLikeFromCurrentPerson(person, liked)) {
            Like like = new Like();
            like.setEntity(liked);
            like.setPerson(person);
            like.setTime(LocalDateTime.now());
            likesRepository.save(like);
        }
        return getLikesByType(liked.getId(), likeRequest.getType());
    }

    public CommonResponse<LikeResponse> getLikesByType(long entityId, String type) {
        List<Like> likes = likesRepository.findLikesByEntity(type, entityId);
        List<Long> users = likes.stream().map(l -> l.getPerson().getId()).collect(Collectors.toList());
        LikeResponse likeResponse = LikeResponse.builder()
                .likes(likes.size())
                .users(users)
                .build();
        return CommonResponse.<LikeResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(likeResponse)
                .build();
    }

    public boolean validateLikeFromCurrentPerson(Person person, Liked liked) {
        Like like = likesRepository.findLikesByPersonAndEntity(String.valueOf(liked.getType()), liked.getId(), person.getId()).stream().findFirst().orElse(null);
        return like == null || !person.getId().equals(like.getPerson().getId());
    }

    public CommonResponse<LikeResponse> deleteLike(long entityId, String type) {
        Person person = personsService.getPersonByEmail((SecurityContextHolder.getContext().getAuthentication().getName()));
        likesRepository.delete(
                likesRepository.findLikesByPersonAndEntity(
                        String.valueOf(LikeTypes.getType(type)), entityId, person.getId()).stream().findFirst().get());
        return getLikesByType(entityId, type);
    }

    @Named("getLikesList")
    public List<Like> getLikesList(Liked liked, LikeTypes type) {
        return likesRepository.findLikesByEntity(String.valueOf(type), liked.getId());
    }
}
