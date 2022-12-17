package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import soialNetworkApp.model.enums.PostTypes;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ApiModel(description = "info about post")
public class PostResponse {

    @ApiModelProperty(value = "post id", example = "1")
    private Long id;

    @ApiModelProperty(value = "when the post was created", example = "2022-02-24 06:14:36.000000")
    private LocalDateTime time;

    @ApiModelProperty(value = "info about author of post")
    private PersonResponse author;

    @ApiModelProperty(value = "post title", example = "TITLE")
    private String title;

    @ApiModelProperty(value = "number of likes", example = "10")
    private Integer likes;

    @ApiModelProperty(value = "post tags", example = "[funny,summer]")
    private List<String> tags;

    @ApiModelProperty(value = "comments under post")
    private List<CommentResponse> comments;

    @ApiModelProperty(value = "post type", example = "POSTED")
    private PostTypes type;

    @ApiModelProperty(value = "post text", example = "some text")
    @JsonProperty("post_text")
    private String postText;

    @ApiModelProperty(value = "is blocked by current user", example = "false")
    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @ApiModelProperty(value = "did i like the post", example = "true")
    @JsonProperty("my_like")
    private Boolean myLike;

    @Override
    public String toString() {
        return "PostResponse{" +
                "postId=" + id +
                ", authorId=" + author.getId() +
                ", title='" + (title.length() < 10 ? title : (title.substring(0, 8) + "..")) +
                "', likes=" + likes +
                ", tagsCount=" + tags.size() +
                ", commentsCount=" + comments.size() +
                ", isBlocked=" + isBlocked +
                ", myLike=" + myLike +
                '}';
    }
}
