package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class RegisterRs {
    private String error;
    private String email;
    private int timestamp;
    ComplexRs data;
    private String error_description;
}
