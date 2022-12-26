package soialNetworkApp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "data for edit or create comment")
public class CommentRq {

    @ApiModelProperty(value = "parent comment id", example = "1")
    @JsonProperty("parent_id")
    private long parentId;

    @ApiModelProperty(value = "text of comment", example = "some text")
    @JsonProperty("comment_text")
    private String commentText;
//    @JsonProperty("get_deleted")
//    private boolean getDeleted;
}
