package soialNetworkApp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "data for put or delete like")
public class LikeRequest {

    @ApiModelProperty(value = "type of item: Post, Comment", example = "Post")
    private String type;

    @ApiModelProperty(value = "item id", example = "1")
    @JsonProperty("item_id")
    private long itemId;
}
