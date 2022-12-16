package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ApiModel(description = "default user representation")
public class CommentResponse {

    @ApiModelProperty(value = "comment id", example = "1")
    private Long id;

    @ApiModelProperty(value = "comment creation time", example = "2022-02-24 06:14:36.000000")
    private LocalDateTime time;

    @ApiModelProperty(value = "post id for this comment", example = "2")
    @JsonProperty("post_id")
    private Long postId;

    @ApiModelProperty(value = "id of parent comment", example = "1")
    @JsonProperty("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "author of comment", example = "Максим Иванов")
    private PersonResponse author;

    @ApiModelProperty(value = "text od comment", example = "Some text")
    @JsonProperty("comment_text")
    private String commentText;

    @ApiModelProperty(value = "comment is blocked by current user", example = "false")
    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @ApiModelProperty(value = "comment is deleted by current user", example = "false")
    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    @ApiModelProperty(value = "list of embedded comments")
    @JsonProperty("sub_comments")
    private List<CommentResponse> embeddedComments;

    @ApiModelProperty(value = "number of likes", example = "35")
    private Integer likes;

    @ApiModelProperty(value = "whether the current user liked it", example = "false")
    @JsonProperty("my_like")
    private Boolean myLike;

    @Override
    public String toString() {
        return "CommentResponse{" +
                "commentId=" + id +
                ", postId=" + postId +
                ", parentCommentId=" + parentId +
                ", authorId=" + author.getId() +
                ", isBlocked=" + isBlocked +
                ", isDeleted=" + isDeleted +
                ", embeddedCommentsCount=" + embeddedComments.size() +
                ", likes=" + likes +
                ", myLike=" + myLike +
                '}';
    }
}
