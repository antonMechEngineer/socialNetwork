package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "another repeating object")
public class UserRs {

    @ApiModelProperty(value = "some field", example = "some example")
    private long timestamp;

    @ApiModelProperty(value = "some field", example = "some example")
    private PersonRs data;
}
