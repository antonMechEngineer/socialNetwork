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
public class MessageWs {
    private Long id;
    @JsonProperty("dialog_id")
    private Long dialogId;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("author_id")
    private Long authorId;
    @JsonProperty("recipient_id")
    private Long recipientId;
    private Long time;
    @JsonProperty("read_status")
    private String readStatus;
    private String token;
    private Boolean typing;
}
