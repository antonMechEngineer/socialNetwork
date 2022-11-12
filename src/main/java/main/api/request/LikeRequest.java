package main.api.request;

import lombok.Data;
import main.model.enums.PostLikeTypes;

@Data
public class LikeRequest {

    private PostLikeTypes type;
    private long itemId;
}
