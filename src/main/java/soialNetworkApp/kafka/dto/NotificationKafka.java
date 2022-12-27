package soialNetworkApp.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.NotificationTypes;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
public class NotificationKafka {

    private NotificationTypes notificationType;

    private LocalDateTime sentTime;

    private Long notificationedId;

    private Long personId;

    private Boolean isRead;


}
