package soialNetworkApp.api.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTypingWs {
    private boolean typing;
    @JsonProperty("user_id")
    private Long userId;
    private String token;
}
