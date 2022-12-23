package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
@ApiModel(description = "info about message")
public class MessageRs {

    @ApiModelProperty(value = "message id", example = "1")
    private Long id;

    @ApiModelProperty(value = "message time", example = "2022-02-24 06:14:36.000000")
    private ZonedDateTime time;

    @ApiModelProperty(value = "is my message", example = "true")
    private Boolean isSentByMe;

    @ApiModelProperty(value = "message author id", example = "1")
    @JsonProperty("author_id")
    private Long authorId;

    @ApiModelProperty(value = "message recipient id", example = "2")
    @JsonProperty("recipient_id")
    private Long recipientId;

    @ApiModelProperty(value = "message text", example = "some text")
    @JsonProperty("message_text")
    private String messageText;

    @ApiModelProperty(value = "read or unread message", example = "read")
    @JsonProperty("read_status")
    private String readStatus;

    @ApiModelProperty(value = "info about message recipient")
    private PersonResponse recipient;
}
