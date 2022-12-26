package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageTypingWsRs {
    @JsonProperty("user_id")
    private Long userId;
    private Long dialogId;
    private Boolean typing;
}
