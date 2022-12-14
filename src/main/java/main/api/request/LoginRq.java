package main.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "data for login")
public class LoginRq {

    @ApiModelProperty(value = "email of registered user", required = true, example = "fullname@gmail.com")
    private String email;

    @ApiModelProperty(value = "password of registered user", required = true, example = "123qwerty")
    private String password;
}
