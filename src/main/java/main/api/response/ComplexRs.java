package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class ComplexRs {
    private int id;
    private int count;
    private String message;
    private int message_id;

    public ComplexRs(String message) {
        this.message = message;
    }
}
