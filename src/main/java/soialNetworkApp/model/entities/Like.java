package soialNetworkApp.model.entities;

import lombok.Data;
import soialNetworkApp.model.entities.interfaces.Liked;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.LikeTypes;
import soialNetworkApp.model.enums.NotificationTypes;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "likes")
public class Like implements Notificationed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person author;

    @Any(metaDef = "likesMetaDef",metaColumn = @Column(name = "type"))
    @AnyMetaDef(name = "likesMetaDef", idType = "long", metaType = "string", metaValues = {
            @MetaValue(targetEntity = Post.class, value = "POST"),
            @MetaValue(targetEntity = Comment.class, value = "COMMENT")
    })
    @JoinColumn(name = "entity_id")
    private Liked entity;

    @Column(nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private LikeTypes type;

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", personId=" + author.getId() +
                ", entity=" + entity.getId() +
                ", type=" + type +
                '}';
    }

    @Override
    public NotificationTypes getNotificationType() {
        return NotificationTypes.POST_LIKE;
    }

    @Override
    public String getSimpleInfo() {
        return (entity.getType().equals(LikeTypes.POST) ? "post \"" : "comment by post \"") + entity.getParentName() + "\"";
    }
}
