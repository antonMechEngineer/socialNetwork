package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class CommonResponse<T> {

    private String error;
    private Long timestamp;
    private Integer offset;
    private Integer perPage;
    private Long total;
    private T data;
    @JsonProperty("error_description")
    private String errorDescription;
}
