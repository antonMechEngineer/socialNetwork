package main.model.entities;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
@Entity
@Data
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "status_id")
    private FriendshipStatus friendshipStatus;

    @JoinColumn(name = "sent_time")
    private Timestamp sentTime;

    @ManyToOne
    @JoinColumn(name = "src_person_id")
    private Person srcPerson;

    @ManyToOne
    @JoinColumn(name = "dst_person_id")
    private Person dstPerson;

}
