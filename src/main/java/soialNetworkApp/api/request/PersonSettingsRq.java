package soialNetworkApp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonSettingsRq {

    @JsonProperty("notification_type")
    private String notificationType;
    private boolean enable;
}
