package main.model.entities;

import lombok.Data;
import main.model.enums.MessagePermissionTypes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "persons", indexes = @Index(name = "full_name_index", columnList = "first_name, last_name", unique = true))
public class Person {

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

    @Column(nullable = false)
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
    @JoinTable(name = "friendships", joinColumns = @JoinColumn(name = "src_person_id"), inverseJoinColumns = @JoinColumn(name = "dst_person_id"))
    private List<Person> srcFriendships = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "friendships", joinColumns = @JoinColumn(name = "dst_person_id"), inverseJoinColumns = @JoinColumn(name = "src_person_id"))
    private List<Person> dstFriendships = new ArrayList<>();



    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "friendships", joinColumns = @JoinColumn(name = "id"))
    private List<Friendship> srcPersonsFriendships = new ArrayList<>();
    

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<BlockHistory> blockHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private PersonSettings personSettings;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "firstPerson", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Dialog> firstPersonDialogs = new ArrayList<>();

    @OneToMany(mappedBy = "secondPerson", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Dialog> secondPersonDialogs = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + firstName + ' ' + lastName +
                "', email='" + email +
                "', isBlocked=" + isBlocked +
                ", isDeleted=" + isDeleted +
                ", postsCount=" + posts.size() +
                ", commentsCount=" + comments.size() +
                ", messagesCount=" + messages.size() +
                ", likesCount=" + likes.size() +
                '}';
    }
}
