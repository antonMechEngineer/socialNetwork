package main.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@ApiModel(description = "info about currency in current city")
public class CurrencyRateRs {

    @ApiModelProperty(value = "info about USD in current city", example = "60")
    private String usd;

    @ApiModelProperty(value = "info about EUR in current city", example = "65")
    private String euro;
}
