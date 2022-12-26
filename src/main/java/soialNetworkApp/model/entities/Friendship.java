package soialNetworkApp.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.model.enums.NotificationTypes;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "friendships")
public class Friendship implements Notificationed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sent_time", nullable = false)
    private LocalDateTime sentTime;

    @ManyToOne
    @JoinColumn(name = "src_person_id", nullable = false)
    private Person srcPerson;

    @ManyToOne
    @JoinColumn(name = "dst_person_id", nullable = false)
    private Person dstPerson;

    @Column(name = "status_name", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private FriendshipStatusTypes friendshipStatus;

    @Override
    public NotificationTypes getNotificationType() {
        return NotificationTypes.FRIEND_REQUEST;
    }

    @Override
    public Person getAuthor() {
        return dstPerson;
    }

    public Friendship(LocalDateTime sentTime, Person srcPerson, Person dstPerson, FriendshipStatusTypes friendshipStatus) {
        this.sentTime = sentTime;
        this.srcPerson = srcPerson;
        this.dstPerson = dstPerson;
        this.friendshipStatus = friendshipStatus;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", sentTime=" + sentTime +
                ", srcPersonId=" + srcPerson.getId() +
                ", dstPersonId=" + dstPerson.getId() +
                ", friendshipStatusId=" + friendshipStatus +
                '}';
    }
}
