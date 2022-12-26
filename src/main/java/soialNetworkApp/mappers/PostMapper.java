package soialNetworkApp.mappers;

import soialNetworkApp.api.request.PostRq;
import soialNetworkApp.api.response.PostRs;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.Post;
import soialNetworkApp.model.enums.PostTypes;
import soialNetworkApp.service.CommentsService;
import soialNetworkApp.service.LikesService;
import soialNetworkApp.service.TagsService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        uses = {PersonMapper.class, CommentsService.class, TagsService.class, LikesService.class})
public interface PostMapper {

    @Mapping(target = "likes", source = "post", qualifiedByName = "getLikesCount")
    @Mapping(target = "tags", qualifiedByName = "getStringsByTags")
    @Mapping(target = "comments", qualifiedByName = "commentsToResponse")
    @Mapping(target = "type", source = "post")
    @Mapping(target = "myLike", source = "post", qualifiedByName = "getMyLike")
    PostRs postToResponse(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", source = "time")
    @Mapping(target = "author", source = "person")
    @Mapping(target = "isBlocked", defaultValue = "false")
    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "timeDelete", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "tags", qualifiedByName = "getTagsByStrings")
    @Mapping(target = "postFiles", ignore = true)
    Post postRequestToNewPost(PostRq request, Person person, LocalDateTime time);

    default PostTypes getPostType(Post post) {
        return PostTypes.getType(post.getIsDeleted(), post.getTime());
    }
}
