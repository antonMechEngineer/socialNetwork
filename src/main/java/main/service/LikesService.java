package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.LikeRequest;
import main.api.response.CommonResponse;
import main.api.response.LikeResponse;
import main.model.entities.Like;
import main.model.entities.Liked;
import main.model.entities.Person;
import main.model.enums.LikeTypes;
import main.repository.CommentsRepository;
import main.repository.LikesRepository;
import main.repository.PersonsRepository;
import main.repository.PostsRepository;
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
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;
    private final PersonsRepository personsRepository;

    public CommonResponse<LikeResponse> putLike(LikeRequest likeRequest) {
        Person person = getPerson();
        Liked liked = getLikedEntity(likeRequest.getItemId(), likeRequest.getType());
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
        List<Like> likes = likesRepository.findLikesByEntity(LikeTypes.getType(type), getLikedEntity(entityId, type));
        List<Long> users = likes.stream().map(like -> like.getPerson().getId()).collect(Collectors.toList());
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
        Like like = likesRepository.findLikeByPersonAndEntity(liked.getType(), liked, person).orElse(null);
        return like == null || !person.getId().equals(like.getPerson().getId());
    }

    public CommonResponse<LikeResponse> deleteLike(long entityId, String type) {
        Person person = getPerson();
        Liked liked = getLikedEntity(entityId, type);
        likesRepository.delete(
                likesRepository.findLikeByPersonAndEntity(LikeTypes.getType(type), liked, person).get());
        return getLikesByType(entityId, type);
    }

    private Liked getLikedEntity(long entityId, String type) {
        Liked liked;
        switch (type) {
            case "Post" : {
                liked = postsRepository.findById(entityId).orElse(null);
                break;
            }
            case "Comment" : {
                liked = commentsRepository.findById(entityId).orElse(null);
                break;
            }
            default: liked = null;
        }
        return liked;
    }

    private Person getPerson() {
        return personsRepository.findPersonByEmail((SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElse(null);
    }

    @Named("getLikesCount")
    public Integer getLikesCount(Liked liked) {
        return likesRepository.findLikesByEntity(liked.getType(), liked).size();
    }

    @Named("getMyLike")
    public Boolean getMyLike(Liked liked) {
        Person person = getPerson();
        Like like = likesRepository.findLikeByPersonAndEntity(liked.getType(), liked, person).orElse(null);
        return like != null && person.getId().equals(like.getPerson().getId());
    }
}
