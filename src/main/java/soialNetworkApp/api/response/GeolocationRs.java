package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "Cities and countries response")
public class GeolocationRs {

    @ApiModelProperty(value = "The name of geolocation object", example = "Россия")
    private String title;
}
