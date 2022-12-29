package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(description = "another repeating object")
public class UserRs implements Serializable {
    private static final long serialVersionUID = -4439114469417994311L;

    @ApiModelProperty(value = "some field", example = "some example")
    private long timestamp;

    @ApiModelProperty(value = "some field", example = "some example")
    private PersonRs data;
}
