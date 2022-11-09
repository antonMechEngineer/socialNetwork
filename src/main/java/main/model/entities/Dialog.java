package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "dialog")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_person_id")
    private long firstPersonID;
    @Column(name = "second_person_id")
    private long secondPersonID;
    @Column(name = "last_message_id")
    private long lastMessageID;
    @Column(name = "last_time_active")
    private Timestamp lastTimeActive;

}
