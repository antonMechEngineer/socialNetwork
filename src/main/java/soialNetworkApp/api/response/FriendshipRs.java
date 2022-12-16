package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "response for work friendships")
public class FriendshipRs {

    @ApiModelProperty(value = "time of operation", example = "2022-02-24 06:14:36.000000")
    String timestamp;

    ComplexRs data;
}
