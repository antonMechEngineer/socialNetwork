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

    @Column(name = "post_comment_notification", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean postCommentNotification;

    @Column(name = "comment_comment_notification", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean commentCommentNotification;

    @Column(name = "friend_request_notification", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean friendRequestNotification;

    @Column(name = "message_notification", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean messageNotification;

    @Column(name = "friend_birthday_notification", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean friendBirthdayNotification;

    @Column(name = "like_notification", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean likeNotification;

    @Column(name = "post_notification", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean postNotification;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;

    @Override
    public String toString() {
        return "PersonSettings{" +
                "id=" + id +
                ", postCommentNotification=" + postCommentNotification +
                ", commentCommentNotification=" + commentCommentNotification +
                ", friendRequestNotification=" + friendRequestNotification +
                ", messageNotification=" + messageNotification +
                ", friendBirthdayNotification=" + friendBirthdayNotification +
                ", likeNotification=" + likeNotification +
                ", postNotification=" + postNotification +
                ", personId=" + person.getId() +
                '}';
    }
}
