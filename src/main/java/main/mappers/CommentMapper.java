package main.mappers;

import main.api.request.CommentRequest;
import main.api.response.CommentResponse;
import main.model.entities.Comment;
import main.model.entities.Person;
import main.model.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface CommentMapper {

    @Mapping(target = "author" , source = "person")
    @Mapping(target = "parentId" , source = "parentComment.id")
    @Mapping(target = "postId" , source = "post.id")
    @Mapping(target = "embeddedComments" , ignore = true)
    @Mapping(target = "likes" , constant = "0")
    @Mapping(target = "myLike" , constant = "false")
    CommentResponse commentToResponse(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", source = "time")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "parentComment", source = "parentComment")
    @Mapping(target = "embeddedComments" , ignore = true)
    @Mapping(target = "person", source = "person")
    @Mapping(target = "commentText", source = "request.commentText")
    @Mapping(target = "isBlocked" , constant = "false")
    @Mapping(target = "isDeleted" , constant = "false")
    Comment commentRequestToNewComment(CommentRequest request, Post post, Person person, Comment parentComment, LocalDateTime time);
}
