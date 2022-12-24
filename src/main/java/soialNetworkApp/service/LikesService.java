package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.request.LikeRq;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.LikeResponse;
import soialNetworkApp.model.entities.Like;
import soialNetworkApp.model.entities.interfaces.Liked;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.repository.CommentsRepository;
import soialNetworkApp.repository.LikesRepository;
import soialNetworkApp.repository.PostsRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;
    private final PersonsService personsService;
    private final NotificationsService notificationsService;

    @Value("${socialNetwork.timezone}")
    private String timezone;

    public CommonRs<LikeResponse> putLike(LikeRq likeRq) {
        Person person = personsService.getPersonByContext();
        Liked liked = getLikedEntity(likeRq.getItemId(), likeRq.getType());
        if (getLikeFromCurrentPerson(person, liked) == null) {
            Like like = new Like();
            like.setEntity(liked);
            like.setAuthor(person);
            like.setTime(LocalDateTime.now(ZoneId.of(timezone)));
            likesRepository.save(like);
            if (person.getPersonSettings() != null && person.getPersonSettings().getLikeNotification()) {
                notificationsService.createNotification(like, liked.getAuthor());
            }
        }
        return getLikesResponse(liked);
    }

    private CommonRs<LikeResponse> getLikesResponse(Liked liked) {
        List<Like> likes = likesRepository.findLikesByEntity(liked.getType(), liked);
        List<Long> users = likes.stream().map(like -> like.getAuthor().getId()).collect(Collectors.toList());
        LikeResponse likeResponse = LikeResponse.builder()
                .likes(likes.size())
                .users(users)
                .build();
        return buildCommonResponse(likeResponse);
    }

    public CommonRs<LikeResponse> getLikesResponse(long entityId, String type) {
        return getLikesResponse(getLikedEntity(entityId, type));
    }

    public CommonRs<LikeResponse> deleteLike(long entityId, String type) {
        Person person = personsService.getPersonByContext();
        Liked liked = getLikedEntity(entityId, type);
        Like like = getLikeFromCurrentPerson(person, liked);
        if (like != null) {
            notificationsService.deleteNotification(like);
            likesRepository.delete(like);
        }
        return getLikesResponse(liked);
    }

    private Like getLikeFromCurrentPerson(Person person, Liked liked) {
        return likesRepository.findLikeByPersonAndEntity(liked.getType(), liked, person).orElse(null);
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

    @Named("getLikesCount")
    public Integer getLikesCount(Liked liked) {
        return likesRepository.findLikesByEntity(liked.getType(), liked).size();
    }

    @Named("getMyLike")
    public Boolean getMyLike(Liked liked) {
        Person person = personsService.getPersonByContext();
        Like like = likesRepository.findLikeByPersonAndEntity(liked.getType(), liked, person).orElse(null);
        return like != null && person.getId().equals(like.getAuthor().getId());
    }

    private CommonRs<LikeResponse> buildCommonResponse(LikeResponse likeResponse) {
        return CommonRs.<LikeResponse>builder()
                .timestamp(System.currentTimeMillis())
                .data(likeResponse)
                .build();
    }
}
