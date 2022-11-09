package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
@Entity
@Getter
@Setter
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @Column(name = "status_id")
    private FriendshipStatus friendshipStatus;

    @Column(name = "sent_time")
    private Timestamp sentTime;

    @ManyToOne
    @Column(name = "src_person_id")
    private Person srcPerson;

    @ManyToOne
    @Column(name = "dst_person_id")
    private Person dstPerson;

}
