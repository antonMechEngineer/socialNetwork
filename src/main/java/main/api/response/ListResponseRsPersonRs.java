package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ListResponseRsPersonRs {
    private String error;
    private Long time;
    private Integer total;
    private List<PersonRs> data;
    private Integer itemPerPage;
    @JsonProperty("error_description")
    private String errorDescription;
}
