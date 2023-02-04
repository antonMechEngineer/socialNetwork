package socialnet.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "common error response")
public class ErrorRs {

    @ApiModelProperty(value = "name of error", required = true, example = "EmptyEmailException")
    private final String error;

    @ApiModelProperty(value = "description of error", required = true, example = "Field 'email' is empty")
    @JsonProperty("error_description")
    private final String errorDescription;

    @ApiModelProperty(value = "error time in timestamp", required = true, example = "12432857239")
    private final Long timestamp;
}
