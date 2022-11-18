package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import main.model.enums.FriendshipStatusTypes;
import main.model.enums.MessagePermissionTypes;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class PersonResponse {

    private Long id;

    private String email;

    private String phone;

    @JsonProperty(defaultValue = "photo")
    private String photo;

    private String about;

    private String city;

    private String country;

    private String token;

    private WeatherRs weather;

    private CurrencyRateRs currency;

    private Boolean online;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("reg_date")
    private LocalDateTime regDate;

    @JsonProperty("birth_date")
    private LocalDateTime birthDate;

    @JsonProperty("messages_permission")
    private MessagePermissionTypes messagePermission;

    @JsonProperty("last_online_time")
    private LocalDateTime lastOnlineTime;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @JsonProperty("friend_status")
    private FriendshipStatusTypes friendStatus;

    @JsonProperty("user_deleted")
    private Boolean isDeleted;
}
