package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class MessageWsRs {
    private Long id;
    @JsonProperty("author_id")
    private Long authorId;
    @JsonProperty("recipient_id")
    private Long recipientId;
    private Boolean isSentByMe;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("message_text")
    private String messageText;
    private Long time;
}
