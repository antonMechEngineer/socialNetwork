package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
@ApiModel(description = "weather in city")
public class WeatherRs {

    @ApiModelProperty(value = "cloudiness in current city", example = "clouds")
    private String clouds;

    @ApiModelProperty(value = "temperature in current city", example = "9")
    private String temp;

    @ApiModelProperty(value = "name of current city", example = "Paris")
    private String city;
}
