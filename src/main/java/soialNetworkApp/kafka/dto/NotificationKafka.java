package soialNetworkApp.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.NotificationTypes;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class NotificationKafka {
    @JsonProperty("notification_type")
    private NotificationTypes notificationType;

    @JsonProperty("sent_time")
    private LocalDateTime sentTime;

    private Notificationed entity;

    private Person person;

    private Boolean isRead;


}
