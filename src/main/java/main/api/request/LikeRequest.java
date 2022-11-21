package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LikeRequest {

    private String type;
    @JsonProperty("item_id")
    private long itemId;
}
