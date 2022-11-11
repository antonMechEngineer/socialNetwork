package main.config.entities;
import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "reg_date")
    private Timestamp regDate;

    @Column(name = "birth_date")
    private Timestamp birthDate;

    private String email;
    private String phone;
    private String password;
    private String photo;
    private String about;
    private String city;
    private String country;

    @Column(name = "confirmation_code")
    private Long confirmationCode;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column (name = "message_permission")
    private String messagePermission;

    @Column(name = "last_online_time")
    private Timestamp lastOnlineTime;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column (name = "change_password_token")
    private String changePasswordToken;

    @Column (name = "notifications_session_id")
    private String notificationSessionID;

    @Column (name = "online_status")
    private String onlineStatus;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_time")
    private Timestamp deletedTime;

    @OneToMany(mappedBy = "srcPerson", cascade = CascadeType.ALL)
    private List<Friendship> friendships;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<BlockHistory> blockHistories;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PostComment> postComments;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PersonSettings> personSettings;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "firstPerson", cascade = CascadeType.ALL)
    private List<Dialog> firstPersonDialogs;

    @OneToMany(mappedBy = "secondPerson", cascade = CascadeType.ALL)
    private List<Dialog> secondPersonDialogs;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PostLike> postLikes;



}
