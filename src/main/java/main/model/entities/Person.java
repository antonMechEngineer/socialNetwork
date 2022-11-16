package main.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.response.CurrencyRateRs;
import main.api.response.WeatherRs;
import main.model.enums.FriendshipStatusTypes;
import main.model.enums.MessagePermissionTypes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "persons", indexes = @Index(name = "full_name_index", columnList = "first_name, last_name", unique = true))
//@JsonInclude(NON_NULL)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;

    @Column(name = "reg_date", nullable = false)
    @JsonProperty("reg_date")
    private LocalDateTime regDate;

    @Column(name = "birth_date")
    @JsonProperty("birth_date")
    private LocalDateTime birthDate;

    private String email;

    private String phone;

    @JsonIgnore
    private String password;

    @JsonProperty(defaultValue = "dfgdfgdfg")
    private String photo;

    private String about;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonIgnore
    private City city;

    @Column(name = "confirmation_code")
    @JsonIgnore
    private Integer confirmationCode;

    @Column(name = "is_approved", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonIgnore
    private Boolean isApproved;

    @Column (name = "message_permission", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ALL'")
    @Enumerated(EnumType.STRING)
    @JsonProperty("messages_permission")
    private MessagePermissionTypes messagePermission;

    @Column(name = "last_online_time")
    @JsonProperty("last_online_time")
    private LocalDateTime lastOnlineTime;

    @Column(name = "is_blocked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @Column (name = "change_password_token")
    @JsonIgnore
    private String changePasswordToken;

    @Column (name = "notifications_session_id")
    @JsonIgnore
    private String notificationSessionID;

    @Column (name = "online_status")
    @JsonProperty("online")
    private String onlineStatus;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonProperty("user_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_time")
    @JsonIgnore
    private LocalDateTime deletedTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "friendship", joinColumns = @JoinColumn(name = "src_person_id"), inverseJoinColumns = @JoinColumn(name = "dst_person_id"))
    @JsonIgnore
    private List<Person> srcFriendships;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "friendship", joinColumns = @JoinColumn(name = "dst_person_id"), inverseJoinColumns = @JoinColumn(name = "src_person_id"))
    @JsonIgnore
    private List<Person> dstFriendships;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BlockHistory> blockHistoryList;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    @JsonIgnore
    private PersonSettings personSettings;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> notifications;

    @OneToMany(mappedBy = "firstPerson", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Dialog> firstPersonDialogs;

    @OneToMany(mappedBy = "secondPerson", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Dialog> secondPersonDialogs;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Message> messages;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PostLike> postLikes;

    @Transient
    private String token;

    @JsonProperty
    private String city() {
        return city.getTitle();
    }

    @JsonProperty
    private String country() {
        return city.getCountry().getTitle();
    }

    @JsonProperty
    private WeatherRs weather() {
        return WeatherRs.builder().clouds("").temp("").city(city.getTitle()).build();
    }

    @JsonProperty
    private CurrencyRateRs currency() {
        return CurrencyRateRs.builder().usd("").euro("").build();
    }

    @JsonProperty("friend_status")
    private FriendshipStatusTypes friendStatus() {
        return FriendshipStatusTypes.REQUEST;
    }
}
