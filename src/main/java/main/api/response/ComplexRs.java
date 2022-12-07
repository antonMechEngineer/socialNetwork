package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(NON_NULL)
public class ComplexRs {
    private Integer id;
    private int count;
    private String message;
    private int message_id;

    public ComplexRs(String message) {
        this.message = message;
    }

    public ComplexRs(Integer id, String message) {
        this.id = id;
        this.message = message;
    }
}
