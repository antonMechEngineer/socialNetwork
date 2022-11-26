package main.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Data
@Table(name = "friendships")
public class Friendship {

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

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id", nullable = false)
    private FriendshipStatus friendshipStatus;

    public Friendship(LocalDateTime sentTime, Person srcPerson, Person dstPerson, FriendshipStatus friendshipStatus) {
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
                ", friendshipStatusId=" + friendshipStatus.getId() +
                '}';
    }
}
