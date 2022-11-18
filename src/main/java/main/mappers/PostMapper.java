package main.mappers;

import main.api.response.CommentResponse;
import main.api.response.PostResponse;
import main.model.entities.Post;
import main.model.entities.Tag;
import main.model.enums.PostTypes;
import main.service.CommentsService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {PersonMapper.class})
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "likes", expression = "java(post.getPostLikes().size())")
    @Mapping(target = "tags", source = "post")
    @Mapping(target = "comments", source = "post")
    @Mapping(target = "type", source = "post")
    @Mapping(target = "myLike" , constant = "false")
    PostResponse postToResponse(Post post);


    default List<String> tagsToString(Post post) {
        return post.getTags().stream().map(Tag::getTagName).collect(Collectors.toList());
    }

    default List<CommentResponse> getCommentsResponse(Post post) {
        return new ArrayList<>(CommentsService.commentsToResponse(post.getComments()));
    }

    default PostTypes getPostType(Post post) {
        return PostTypes.getType(post.getIsDeleted(), post.getTime());
    }
}
