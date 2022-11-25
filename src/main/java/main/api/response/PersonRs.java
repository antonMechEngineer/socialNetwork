package main.api.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.entities.Currency;
import main.model.enums.FriendshipStatusTypes;
import main.model.enums.MessagePermissionTypes;

@Data
public class PersonRs {
    private Long id;
    private String email;
    private String phone;
    private String about;
    private String city;
    private String country;
    private String token;
    private WeatherRs weatherRs;
    private Currency currency;
    private Boolean online;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("reg_date")
    private Long regDate;

    @JsonProperty("birth_date")
    private Long birthDate;

    @JsonProperty("messages_permission")
    private MessagePermissionTypes messagePermission;

    @JsonProperty("last_online_time")
    private Long lastOnlineTime;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @JsonProperty("friend_status")
    private FriendshipStatusTypes friendStatus;
}
