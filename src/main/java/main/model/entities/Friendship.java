package main.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @JoinColumn(name = "sent_time", nullable = false)
    private LocalDateTime sentTime;

    @ManyToOne
    @JoinColumn(name = "src_person_id", nullable = false)
    @ToString.Exclude
    private Person srcPerson;

    @ManyToOne
    @JoinColumn(name = "dst_person_id", nullable = false)
    @ToString.Exclude
    private Person dstPerson;

    @OneToOne
    @JoinColumn(name = "status_id", nullable = false)
    @ToString.Exclude
    private FriendshipStatus friendshipStatus;

    public Friendship(LocalDateTime sentTime, Person srcPerson, Person dstPerson, FriendshipStatus friendshipStatus) {
        this.sentTime = sentTime;
        this.srcPerson = srcPerson;
        this.dstPerson = dstPerson;
        this.friendshipStatus = friendshipStatus;
    }
}
