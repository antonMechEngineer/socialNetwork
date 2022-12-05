package main.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "dialogs")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_active_time")
    private ZonedDateTime lastActiveTime;

    @OneToOne
    @JoinColumn(name = "last_message_id", referencedColumnName = "dialog_id")
    private Message lastMessage;

    @ManyToOne
    @JoinColumn(name = "first_person_id", nullable = false)
    private Person firstPerson;

    @ManyToOne
    @JoinColumn(name = "second_person_id", nullable = false)
    private Person secondPerson;
}
