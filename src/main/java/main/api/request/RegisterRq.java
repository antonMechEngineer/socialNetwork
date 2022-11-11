package main.api.request;

import lombok.Data;

@Data
public class RegisterRq {
    private String email;
    private String password;
}
