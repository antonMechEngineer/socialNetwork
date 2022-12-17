package soialNetworkApp.mappers;

import soialNetworkApp.api.request.CommentRequest;
import soialNetworkApp.api.response.CommentResponse;
import soialNetworkApp.model.entities.Comment;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.Post;
import soialNetworkApp.service.LikesService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", uses = {PersonMapper.class, LikesService.class}, imports = {LocalDateTime.class, ZoneId.class})
public interface CommentMapper {

    @Mapping(target = "parentId", source = "parentComment.id")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "embeddedComments" , ignore = true)
    @Mapping(target = "likes", source = "comment", qualifiedByName = "getLikesCount")
    @Mapping(target = "myLike", source = "comment", qualifiedByName = "getMyLike")
    CommentResponse commentToResponse(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", expression = "java(LocalDateTime.now(ZoneId.of(\"Europe/Moscow\")))")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "parentComment", source = "parentComment")
    @Mapping(target = "embeddedComments", ignore = true)
    @Mapping(target = "author", source = "person")
    @Mapping(target = "commentText", source = "request.commentText")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "isDeleted", constant = "false")
    Comment commentRequestToNewComment(CommentRequest request, Post post, Person person, Comment parentComment);
}
