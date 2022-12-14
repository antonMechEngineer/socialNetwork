package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "data to update the post")
public class PostRequest {

    @ApiModelProperty(value = "new post title", example = "NEW TITLE")
    private String title;

    @ApiModelProperty(value = "new post tags", example = "[winter,boring]")
    private List<String> tags;

    @ApiModelProperty(value = "new post text", example = "some new text")
    @JsonProperty("post_text")
    private String postText;
//    @JsonProperty("get_deleted")
//    private boolean getDeleted;
}
