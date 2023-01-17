package soialNetworkApp.kafka.dto;

import lombok.Builder;
import lombok.Data;
import soialNetworkApp.model.enums.NotificationTypes;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationKafka {

    private NotificationTypes notificationType;

    private LocalDateTime sentTime;

    private Long notificationedId;

    private Long personId;

    private Boolean isRead;


}
