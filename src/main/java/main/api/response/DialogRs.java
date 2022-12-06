package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class DialogRs {
    private Long id;
    @JsonProperty("unread_count")
    private Integer unreadCount;
    @JsonProperty("last_message")
    private MessageRs lastMessage;
    @JsonProperty("author_id")
    private Long authorId;
    @JsonProperty("recipient_id")
    private Long recipientId;
    @JsonProperty("read_status")
    private String readStatus;
}
