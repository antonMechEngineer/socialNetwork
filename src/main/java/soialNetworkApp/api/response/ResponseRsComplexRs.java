package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "response gor recover my data")
public class ResponseRsComplexRs {

    @ApiModelProperty(value = "time of operation", example = "1234535646")
    private long timestamp;

    @ApiModelProperty(value = "page", example = "0")
    private int offset;

    @ApiModelProperty(value = "items on page", example = "20")
    private int perPage;

    @ApiModelProperty(value = "some objext")
    ComplexRs data;
}
