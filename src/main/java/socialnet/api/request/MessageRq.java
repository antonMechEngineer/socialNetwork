package socialnet.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageRq {
    @JsonProperty("dialog_id")
    private Long dialogId;
    @JsonProperty("message_text")
    private String messageText;
}
