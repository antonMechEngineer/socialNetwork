package socialnet.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(description = "info about number and which persons i liked")
public class LikeRs {

    @ApiModelProperty(value = "number of my likes", example = "10")
    private Integer likes;

    @ApiModelProperty(value = "user ids that I liked", example = "[1,2,3]")
    private List<Long> users;
}
