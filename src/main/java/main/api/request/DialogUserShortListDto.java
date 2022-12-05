package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DialogUserShortListDto {
    private Long userId;
    @JsonProperty("users_ids")
    private List<Long> usersIds;
}
