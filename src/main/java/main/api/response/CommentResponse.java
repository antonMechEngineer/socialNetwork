package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponse {

    private Long id;

    private LocalDateTime time;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("parent_id")
    private Long parentId;

    private PersonResponse author;

    @JsonProperty("comment_text")
    private String commentText;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    @JsonProperty("sub_comments")
    private List<CommentResponse> embeddedComments;

    private Integer likes;

    private Boolean myLike;
}
