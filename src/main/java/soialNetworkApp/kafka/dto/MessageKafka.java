package soialNetworkApp.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.enums.ReadStatusTypes;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class MessageKafka {

    private ZonedDateTime time;

    @JsonProperty("message_text")
    private String messageText;

    @JsonProperty("read_status")
    private ReadStatusTypes readStatus;

    private Boolean isDeleted;

    private Person author;

    private Person recipient;

    private Dialog dialog;
}
