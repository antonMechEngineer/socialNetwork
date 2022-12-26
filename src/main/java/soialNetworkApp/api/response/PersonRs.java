package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.model.enums.MessagePermissionTypes;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
@ApiModel(description = "default user representation")
public class PersonRs implements Serializable {
    private static final long serialVersionUID = -4439114469417994311L;

    @ApiModelProperty(value = "user id", example = "1")
    private Long id;

    @ApiModelProperty(value = "user email", example = "fullName@gmail.com")
    private String email;

    @ApiModelProperty(value = "user phone", example = "+7 (982) 281-15-23")
    private String phone;

    @ApiModelProperty(value = "url of user photo", example = "/some/path")
    @JsonProperty(defaultValue = "photo")
    private String photo;

    @ApiModelProperty(value = "info about user", example = "I'm from Paris")
    private String about;

    @ApiModelProperty(value = "city of user", example = "Paris")
    private String city;

    @ApiModelProperty(value = "country of user", example = "France")
    private String country;

    @ApiModelProperty(value = "delete this field", example = "DELETE THIS FIELD")
    private String token;

    @ApiModelProperty(value = "weather in user city")
    private WeatherRs weather;

    @ApiModelProperty(value = "currency in user country")
    private CurrencyRs currency;

    @ApiModelProperty(value = "user is online?", example = "true")
    private Boolean online;

    @JsonProperty("first_name")
    @ApiModelProperty(value = "first name of user", example = "Максим")
    private String firstName;

    @JsonProperty("last_name")
    @ApiModelProperty(value = "last name of user", example = "Иванов")
    private String lastName;

    @JsonProperty("reg_date")
    @ApiModelProperty(value = "user's registration date", example = "2022-02-24 06:14:36.000000")
    private LocalDateTime regDate;

    @JsonProperty("birth_date")
    @ApiModelProperty(value = "user's birthday", example = "2022-02-24 06:14:36.000000")
    private LocalDateTime birthDate;

    @JsonProperty("messages_permission")
    @ApiModelProperty(value = "who can write", example = "ALL")
    private MessagePermissionTypes messagePermission;

    @JsonProperty("last_online_time")
    @ApiModelProperty(value = "the last time the user was online", example = "2022-02-24 06:14:36.000000")
    private LocalDateTime lastOnlineTime;

    @JsonProperty("is_blocked")
    @ApiModelProperty(value = "whether the user is locked out for the current user", example = "false")
    private Boolean isBlocked;

    @JsonProperty("friend_status")
    @ApiModelProperty(value = "relationship of user to current user", example = "FRIEND")
    private FriendshipStatusTypes friendStatus;

    @JsonProperty("user_deleted")
    @ApiModelProperty(value = "is the user deleted", example = "false")
    private Boolean isDeleted;

    @Override
    public String toString() {
        return "PersonRs{" +
                "id=" + id +
                ", email='" + email +
                "', name='" + firstName + ' ' + lastName +
                "', isBlocked=" + isBlocked +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
