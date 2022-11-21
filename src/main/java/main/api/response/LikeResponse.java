package main.api.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LikeResponse {

    private Integer likes;
    private List<Long> users;
}
