package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeolocationRs {

    @ApiModelProperty(value = "The name of geolocation object", example = "Россия")
    private String title;
}
