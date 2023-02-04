package socialnet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "data dor search users")
public class FindPersonRq {

    @ApiModelProperty(value = "possible first name", example = "Максим")
    private String first_name;

    @ApiModelProperty(value = "possible last name", example = "Иванов")
    private String last_name;

    @ApiModelProperty(value = "after this age", example = "4")
    private Integer age_from;

    @ApiModelProperty(value = "before this age", example = "80")
    private Integer age_to;

    @ApiModelProperty(value = "city name", example = "Paris")
    private String city;

    @ApiModelProperty(value = "country name", example = "France")
    private String country;
}
