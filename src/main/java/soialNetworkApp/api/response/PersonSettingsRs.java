package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonSettingsRs {

    @ApiModelProperty(value = "Type of object for notification", example = "POST_COMMENT")
    private String type;

    @ApiModelProperty(value = "Turning on or turning off the notification", example = "true")
    private boolean enable;
}
