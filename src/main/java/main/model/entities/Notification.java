package main.model.entities;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_type")
    private String notificationType;

    @Column(name = "sent_time")
    private Timestamp sentTime;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "entity_id")
    private Long entityId;

    private String contact;

    @Column(name = "is_read")
    private Boolean isRead;

}
