package soialNetworkApp.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "object for password reset")
public class PasswordRq {

    @ApiModelProperty(value = "secret for password reset", required = true, example = "uuid")
    private String secret;

    @ApiModelProperty(value = "new password", required = true, example = "123qwerty")
    private String password;
}
