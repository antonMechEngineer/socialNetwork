package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRs {
    private String error;
    private long timestamp;
    private PersonResponse data;
    private String error_description;
}
