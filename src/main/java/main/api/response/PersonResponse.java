package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import main.model.enums.MessagePermissionTypes;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
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
    private FriendshipStatusTypes friendStatus() {
        return FriendshipStatusTypes.REQUEST;
    }

    @JsonProperty("user_deleted")
    private Boolean isDeleted;

    public PersonResponse(Person person) {
        this.id = person.getId();
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.photo = person.getPhoto();
        this.about = person.getAbout();
        this.city = person.getCity().getTitle();
        this.country = person.getCity().getCountry().getTitle();
        this.weather = WeatherRs.builder().clouds("clouds").temp("9").city(person.getCity().getTitle()).build();
        this.currency = CurrencyRateRs.builder().usd("60").euro("62").build();
        this.online = true;
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.regDate = person.getRegDate();
        this.birthDate = person.getBirthDate();
        this.messagePermission = person.getMessagePermission();
        this.lastOnlineTime = person.getLastOnlineTime();
        this.isBlocked = person.getIsBlocked();
        this.isDeleted = person.getIsDeleted();
    }
}
