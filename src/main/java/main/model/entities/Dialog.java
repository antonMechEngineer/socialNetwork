package main.model.entities;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "dialog")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @Column(name = "first_person_id")
    private Person firstPerson;

    @ManyToOne
    @Column(name = "second_person_id")
    private Person secondPerson;

    @OneToOne
    @Column(name = "last_message_id")
    private Message message;

    @Column(name = "last_time_active")
    private Timestamp lastTimeActive;

    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL)
    private List<Message> messages;

}
