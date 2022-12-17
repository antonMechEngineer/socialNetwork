package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@ApiModel(description = "weather in city")
public class WeatherResponse {

    @ApiModelProperty(value = "cloudiness in current city", example = "clouds")
    private String clouds;

    @ApiModelProperty(value = "temperature in current city", example = "9")
    private String temp;

    @ApiModelProperty(value = "name of current city", example = "Paris")
    private String city;

    @ApiModelProperty(value = "weather update time", example = "2022-12-10T12:00:00")
    private String date;
}
