package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "data about dialog")
public class DialogRs {

    @ApiModelProperty(value = "id of dialog", example = "1")
    private Long id;

    @ApiModelProperty(value = "count of unread message", example = "15")
    @JsonProperty("unread_count")
    private Long unreadCount;

    @ApiModelProperty(value = "last message in dialog")
    @JsonProperty("last_message")
    private MessageRs lastMessage;

    @ApiModelProperty(value = "message author id", example = "1")
    @JsonProperty("author_id")
    private Long authorId;

    @ApiModelProperty(value = "message recipient id", example = "2")
    @JsonProperty("recipient_id")
    private Long recipientId;

    @ApiModelProperty(value = "read or unread message", example = "read")
    @JsonProperty("read_status")
    private String readStatus;
}
