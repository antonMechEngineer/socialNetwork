package soialNetworkApp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageWsRq {
    @JsonProperty("dialog_id")
    private Long dialogId;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("author_id")
    private Long authorId;
    private Long time;
    @JsonProperty("read_status")
    private String readStatus;
    private String token;
    private Boolean typing;
}
