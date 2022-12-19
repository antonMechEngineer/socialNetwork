package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "info about currency in current city")
public class CurrencyResponse {

    @ApiModelProperty(value = "info about USD in current city", example = "60")
    private String usd = "No data available";

    @ApiModelProperty(value = "info about EUR in current city", example = "65")
    private String euro = "No data available";
}
