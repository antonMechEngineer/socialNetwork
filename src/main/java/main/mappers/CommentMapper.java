package main.mappers;

import main.api.response.CommentResponse;
import main.model.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PersonMapper.class})
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "author" , source = "person")
    @Mapping(target = "parentId" , source = "parentComment.id")
    @Mapping(target = "postId" , source = "post.id")
    @Mapping(target = "embeddedComments" , ignore = true)
    @Mapping(target = "likes" , constant = "0")
    @Mapping(target = "myLike" , constant = "false")
    CommentResponse commentToResponse(Comment comment);
}
