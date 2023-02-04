package socialnet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "data for set or recover email")
public class EmailRq {

    @ApiModelProperty(value = "string representation of email", example = "fullName@gmail.com", required = true)
    private String email;
    private String secret;
}
