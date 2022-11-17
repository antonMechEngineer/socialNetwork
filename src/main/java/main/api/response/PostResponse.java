package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.entities.Post;
import main.model.entities.Tag;
import main.model.enums.PostTypes;
import main.service.CommentsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostResponse {

    private Long id;

    private LocalDateTime time;

    private PersonResponse author;

    private String title;

    private Integer likes;

    private List<Tag> tags;

    private List<CommentResponse> comments;

    private PostTypes type;

    @JsonProperty("post_text")
    private String postText;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    private Boolean myLike;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.time = post.getTime();
        this.author = new PersonResponse(post.getAuthor());
        this.title = post.getTitle();
        this.likes = post.getPostLikes().size();
        this.tags = post.getTags();
        this.comments = new ArrayList<>(CommentsService.commentsToResponse(post.getComments()));
        this.type = PostTypes.getType(post.getIsDeleted(), post.getTime());
        this.postText = post.getPostText();
        this.isBlocked = post.getIsBlocked();
        this.myLike = false;
    }
}
