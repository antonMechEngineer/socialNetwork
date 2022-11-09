package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "person_settings")
public class PersonSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @Column(name = "person_id")
    private Person person;

    @Column(name = "post_comment_notification")
    private boolean postCommentNotification;

    @Column(name = "comment_comment_notification")
    private boolean commentCommentNotification;

    @Column(name = "friend_request_notification")
    private boolean friendRequestNotification;

    @Column(name = "message_notification")
    private boolean messageNotification;

    @Column(name = "friend_birthday_notification")
    private boolean friendBirthdayNotification;

    @Column(name = "like_notification")
    private boolean likeNotification;

    @Column(name = "post_notification")
    private boolean postNotification;
}
