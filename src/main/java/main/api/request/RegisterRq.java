package main.api.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class RegisterRq {
    private static final int PASSWORD_LENGTH = 6;
    @Email
    private String email;
    @NotBlank(message = "Введите корректное имя")
    @Pattern(regexp = "[А-Яа-яA-Za-z]",
            message = "Введите корректное имя")
    private String firstName;
    @Pattern(regexp = "[А-Яа-яA-Za-z]",
            message = "Введите корректное имя")
    private String lastName;
    private String code;
    //@JsonProperty("secret_code")
    private String codeSecret;
    @Min(PASSWORD_LENGTH)
    private String passwd1;
    private String passwd2;
}
