package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class MessageWsRq {
    @JsonProperty("dialog_id")
    private Long dialogId;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("author_id")
    private Long authorId;
    private Timestamp time;
    @JsonProperty("read_status")
    private String readStatus;
}
