package main.api.request;

import lombok.Data;

@Data
public class PasswordRq {
    private String secret;
    private String password;
}
