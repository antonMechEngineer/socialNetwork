package soialNetworkApp.kafka.dto;

import lombok.*;
import soialNetworkApp.model.enums.NotificationTypes;

@Getter
@Setter
@RequiredArgsConstructor
public class NotificationKafka {

    private Long id;

    private NotificationTypes notificationType;

    private Long notificationedId;

    private Long personId;

    private Boolean isRead;
}
