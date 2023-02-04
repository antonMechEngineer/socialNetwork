package socialnet.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "info about currency in current city")
public class CurrencyRs implements Serializable {
    private static final long serialVersionUID = -4439114469417994311L;

    @ApiModelProperty(value = "info about USD exchange rate in roubles", example = "60.4312")
    private String usd = "No data available";

    @ApiModelProperty(value = "info about EUR exchange rate in roubles", example = "65.8432")
    private String euro = "No data available";
}
