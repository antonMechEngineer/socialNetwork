package soialNetworkApp.api.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageTypingWs {
    private Boolean typing;
    @JsonProperty("user_id")
    private Long userId;
}
