package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserRs implements Serializable {
    private static final long serialVersionUID = -4439114469417994311L;
    private String error;
    private long timestamp;
    private PersonResponse data;
    private String error_description;
}
