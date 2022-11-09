package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    private long confirmationCode;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Column (name = "message_permission")
    private String messagePermission;

    @Column(name = "last_online_time")
    private Timestamp lastOnlineTime;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column (name = "change_password_token")
    private String changePasswordToken;

    @Column (name = "notifications_session_id")
    private String notificationSessionID;

    @Column (name = "online_status")
    private String onlineStatus;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "deleted_time")
    private Timestamp deletedTime;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Friendship> friendships;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<BlockHistory> blockHistories;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PostComment> postComments;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PersonSettings> personSettings;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Dialog> dialogs;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PostLike> postLikes;

    // TODO: 09.11.2022 можно через join проложить связи до friendship_status и post_file но не уверен что, нужно


}
