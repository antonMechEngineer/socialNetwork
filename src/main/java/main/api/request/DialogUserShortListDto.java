package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@NotNull
@Data
public class DialogUserShortListDto {
    private Long userId;
    @JsonProperty("user_ids")
    private List<Long> userIds;
}
