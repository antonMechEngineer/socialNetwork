package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import main.model.enums.NotificationTypes;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "info about notifications")
public class NotificationResponse {

    @ApiModelProperty(value = "id of notification", example = "1")
    private long id;

    @ApiModelProperty(value = "info about notification", example = "some info")
    private String info;

    @ApiModelProperty(value = "notification type", example = "POST")
    @JsonProperty("notification_type")
    private NotificationTypes notificationType;

    @ApiModelProperty(value = "when notification sent", example = "2022-02-24 06:14:36.000000")
    @JsonProperty("sent_time")
    private LocalDateTime sentTime;

    @ApiModelProperty(value = "info about notification author")
    @JsonProperty("entity_author")
    private PersonResponse entityAuthor;
}
