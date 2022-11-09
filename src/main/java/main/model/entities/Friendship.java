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
    @Column(name = "status_id")
    private long statusID;
    @Column(name = "sent_time")
    private Timestamp sentTime;
    @Column(name = "src_person_id")
    private long srcPersonID;
    @Column(name = "dst_person_id")
    private long dstPersonID;

}
