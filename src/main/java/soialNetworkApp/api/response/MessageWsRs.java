package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageWsRs {
    private Long id;
    @JsonProperty("author_id")
    private Long authorId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("message_text")
    private String messageText;
}
