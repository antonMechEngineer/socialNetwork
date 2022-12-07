package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import main.model.enums.ReadStatusTypes;

import java.time.ZonedDateTime;

@Data
@Builder
public class MessageRs {
    private Long id;
    private ZonedDateTime time;
    private Boolean isSentByMe;
    @JsonProperty("author_id")
    private Long authorId;
    @JsonProperty("recipient_id")
    private Long recipientId;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("read_status")
    private String readStatus;
    private PersonResponse recipient;
}
