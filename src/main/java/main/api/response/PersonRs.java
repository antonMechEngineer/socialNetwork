package main.api.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.entities.Currency;
import main.model.entities.Person;
import main.model.enums.FriendshipStatusTypes;
import main.model.enums.MessagePermissionTypes;

import java.time.ZoneId;

@Data
public class PersonRs {
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

    @JsonProperty("user_deleted")
    private boolean userDeleted;

    public PersonRs(Person person) {
        this.id = person.getId();
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.photo = person.getPhoto();
        this.about = person.getAbout();
        this.city = person.getCity() == null ? null : person.getCity().getTitle();
        this.country = person.getCity() == null ? null : person.getCity().getCountry().getTitle();
        this.weather = person.getCity() == null ? null : WeatherRs.builder()
                .clouds("clouds").temp("9").city(person.getCity().getTitle()).build();
        this.currency = CurrencyRateRs.builder().usd("60").euro("62").build();
        this.online = true;
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.regDate = person.getRegDate().atZone(ZoneId.systemDefault()).toEpochSecond();
        this.birthDate = person.getBirthDate().atZone(ZoneId.systemDefault()).toEpochSecond();
        this.messagePermission = person.getMessagePermission();
        this.lastOnlineTime = person.getLastOnlineTime().atZone(ZoneId.systemDefault()).toEpochSecond();
        this.isBlocked = person.getIsBlocked();
        this.userDeleted = person.getIsDeleted();
    }
}
