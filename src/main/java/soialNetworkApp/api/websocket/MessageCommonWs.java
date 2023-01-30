package soialNetworkApp.api.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soialNetworkApp.model.entities.Message;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCommonWs {
    private Long id;
    @JsonProperty("dialog_id")
    private Long dialogId;
    @JsonProperty("message_ids")
    private List<Long> messageIds;
    @JsonProperty("message_id")
    private Long messageId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("message_text")
    private String messageText;
    private String token;
}
