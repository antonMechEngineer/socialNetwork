package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@ApiModel(description = "weather in city")
public class WeatherRs implements Serializable {
    private static final long serialVersionUID = -4439114469417994311L;

    @ApiModelProperty(value = "Current city's weather description", example = "clouds")
    private String clouds;

    @ApiModelProperty(value = "Temperature in current city", example = "9")
    private String temp;

    @ApiModelProperty(value = "Current city name", example = "Paris")
    private String city;

    @ApiModelProperty(value = "Weather last update time", example = "2022-12-10T12:00:00")
    private String date;
}
