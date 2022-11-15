package main.api.request;

import lombok.Data;

@Data
public class PasswordRq {
    private String token;
    private String password;
}
