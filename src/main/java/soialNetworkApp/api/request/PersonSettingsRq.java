package soialNetworkApp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonSettingsRq {

    @ApiModelProperty(value = "Type of notification object", example = "POST_COMMENT")
    @JsonProperty("notification_type")
    private String notificationType;
    @ApiModelProperty(value = "Turning on or turning off the notification", example = "true")
    private boolean enable;
}
