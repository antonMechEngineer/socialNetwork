package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "notification_type")
    private String notificationType;

    @Column(name = "sent_time")
    private Timestamp sentTime;

    @Column(name = "person_id")
    private Person person;

    @Column(name = "entity_id")
    private long entityID;

    private String contact;

    @Column(name = "is_read")
    private boolean isRead;

}
