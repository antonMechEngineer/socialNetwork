package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "response after set, reset, recovery password or set, recovery email ")
public class RegisterRs {

    @ApiModelProperty(value = "name of error", example = "EmptyEmailException")
    private String error;

    @ApiModelProperty(value = "mail of the user for whom the operations are performed", required = true, example = "fullName@gamil.com")
    private String email;

    @ApiModelProperty(value = "operation time in timestamp", required = true, example = "1670773804")
    private int timestamp;

    ComplexRs data;

    @ApiModelProperty(value = "description of error", example = "Email field is empty")
    private String error_description;
}
