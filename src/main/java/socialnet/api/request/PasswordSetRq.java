package socialnet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "data for set password")
public class PasswordSetRq {

    @ApiModelProperty(value = "installation passwordt", example = "123qwerty")
    private String password;
}
