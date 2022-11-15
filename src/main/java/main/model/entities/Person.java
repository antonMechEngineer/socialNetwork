package main.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import main.model.enums.MessagePermissionTypes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "persons", indexes = @Index(name = "full_name_index", columnList = "first_name, last_name", unique = true))
public class Person {

    // TODO: 15.11.2022 добавить описание колонок согласно swagger, что-то может быть заигнорить то чего не в swagger, как ввести объекты заглушки в Person
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    private String email;

    private String phone;

    private String password;

    private String photo;

    private String about;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "confirmation_code")
    private Integer confirmationCode;

    @Column(name = "is_approved", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isApproved;

    @Column (name = "message_permission", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ALL'")
    @Enumerated(EnumType.STRING)
    private MessagePermissionTypes messagePermission;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnlineTime;

    @Column(name = "is_blocked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isBlocked;

    @Column (name = "change_password_token")
    private String changePasswordToken;

    @Column (name = "notifications_session_id")
    private String notificationSessionID;

    @Column (name = "online_status")
    private String onlineStatus;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "friendship", joinColumns = @JoinColumn(name = "src_person_id"), inverseJoinColumns = @JoinColumn(name = "dst_person_id"))
    private List<Person> srcFriendships;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "friendship", joinColumns = @JoinColumn(name = "dst_person_id"), inverseJoinColumns = @JoinColumn(name = "src_person_id"))
    private List<Person> dstFriendships;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<BlockHistory> blockHistoryList;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private PersonSettings personSettings;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "firstPerson", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Dialog> firstPersonDialogs;

    @OneToMany(mappedBy = "secondPerson", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Dialog> secondPersonDialogs;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Message> messages;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PostLike> postLikes;

    private Currency currency = new Currency();

    private Weather weather = new Weather();

}
