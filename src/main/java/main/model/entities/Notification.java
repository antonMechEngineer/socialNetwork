package main.model.entities;

import lombok.Data;
import main.model.entities.interfaces.Notificationed;
import main.model.enums.NotificationTypes;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_type", insertable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationTypes notificationType;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @Any(metaColumn = @Column(name = "notification_type"), fetch = FetchType.EAGER)
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
            @MetaValue(targetEntity = Post.class, value = "POST"),
            @MetaValue(targetEntity = Comment.class, value = "POST_COMMENT"),
            @MetaValue(targetEntity = Comment.class, value = "COMMENT_COMMENT"),
            @MetaValue(targetEntity = Friendship.class, value = "FRIEND_REQUEST"),
            @MetaValue(targetEntity = Message.class, value = "MESSAGE"),
    })
    @JoinColumn(name = "entity_id")
    private Notificationed entity;

    private String contact;

    @Column(name = "is_read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", notificationType=" + notificationType +
                ", sentTime=" + sentTime +
                ", entity=" + entity.getClass().getName() +
                ", contact='" + contact + '\'' +
                ", isRead=" + isRead +
                ", personId=" + person.getId() +
                '}';
    }
}
