package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.enums.NotificationTypes;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private long id;
    private String info;
    @JsonProperty("notification_type")
    private NotificationTypes notificationType;
    @JsonProperty("sent_time")
    private LocalDateTime sentTime;
    @JsonProperty("entity_author")
    private PersonResponse entityAuthor;
}
