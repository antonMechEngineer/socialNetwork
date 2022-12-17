package soialNetworkApp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@NotNull
@Data
@ApiModel(description = "id's of current user and recipient")
public class DialogUserShortListDto {

    @ApiModelProperty(value = "current user id", example = "1")
    private Long userId;

    @ApiModelProperty(value = "recipient", example = "[2,3,4]")
    @JsonProperty("user_ids")
    private List<Long> userIds;
}
