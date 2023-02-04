package socialnet.kafka.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class MessageKafka {

    private Long id;

    private ZonedDateTime time;

    private String messageText;

    private String readStatus;

    private Boolean isDeleted;

    private Long authorId;

    private Long recipientId;

    private Long dialogId;
}
