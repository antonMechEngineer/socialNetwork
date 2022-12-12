package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageRq {
    @JsonProperty("message_text")
    private String messageText;
}
