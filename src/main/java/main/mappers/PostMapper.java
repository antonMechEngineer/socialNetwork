package main.mappers;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.model.entities.Person;
import main.model.entities.Post;
import main.model.enums.PostTypes;
import main.service.CommentsService;
import main.service.LikesService;
import main.service.TagsService;
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
    PostResponse postToResponse(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", source = "time")
    @Mapping(target = "author", source = "person")
    @Mapping(target = "isBlocked", defaultValue = "false")
    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "timeDelete", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "tags", qualifiedByName = "getTagsByStrings")
    @Mapping(target = "postFiles", ignore = true)
    Post postRequestToNewPost(PostRequest request, Person person, LocalDateTime time);

    default PostTypes getPostType(Post post) {
        return PostTypes.getType(post.getIsDeleted(), post.getTime());
    }
}
