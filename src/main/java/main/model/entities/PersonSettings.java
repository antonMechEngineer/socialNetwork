package main.model.entities;
import lombok.Data;
import javax.persistence.*;
@Entity
@Data
@Table(name = "person_settings")
public class PersonSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "person_id")
    private Person person;

    @Column(name = "post_comment_notification")
    private Boolean postCommentNotification;

    @Column(name = "comment_comment_notification")
    private Boolean commentCommentNotification;

    @Column(name = "friend_request_notification")
    private Boolean friendRequestNotification;

    @Column(name = "message_notification")
    private Boolean messageNotification;

    @Column(name = "friend_birthday_notification")
    private Boolean friendBirthdayNotification;

    @Column(name = "like_notification")
    private Boolean likeNotification;

    @Column(name = "post_notification")
    private Boolean postNotification;
}
