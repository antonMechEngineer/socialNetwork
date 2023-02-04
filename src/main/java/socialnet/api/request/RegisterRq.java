package socialnet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ApiModel(description = "data for user registration")
public class RegisterRq {
    private static final int PASSWORD_LENGTH = 6;
    @Email
    @ApiModelProperty(value = "user registration email", required = true, example = "fullName@gamil.com")
    private String email;

    @ApiModelProperty(value = "first name of new user", required = true, example = "Максим")
    @NotBlank(message = "Введите корректное имя")
    @Pattern(regexp = "[А-Яа-яA-Za-z]",
            message = "Введите корректное имя")
    private String firstName;

    @ApiModelProperty(value = "last name of new user", required = true, example = "Иванов")
    @Pattern(regexp = "[А-Яа-яA-Za-z]",
            message = "Введите корректное имя")
    private String lastName;

    @ApiModelProperty(value = "entered captcha", required = true, example = "zhcyuo")
    private String code;

    @ApiModelProperty(value = "captcha decryption secret", required = true, example = "uuid")
    private String codeSecret;

    @Min(PASSWORD_LENGTH)
    @ApiModelProperty(value = "first password to compare", required = true, example = "123qwerty")
    private String passwd1;

    @ApiModelProperty(value = "second password to compare", required = true, example = "123qwerty")
    private String passwd2;
}
