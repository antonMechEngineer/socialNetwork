package soialNetworkApp.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class MessageKafka {

    private ZonedDateTime time;

    private String messageText;

    private ReadStatusTypes readStatus;

    private Boolean isDeleted;

    private Long authorId;

    private Long recipientId;

    private Long dialogId;
}
