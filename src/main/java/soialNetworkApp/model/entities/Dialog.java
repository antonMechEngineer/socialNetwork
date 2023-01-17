package soialNetworkApp.model.entities;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dialogs")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_active_time")
    private ZonedDateTime lastActiveTime;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message lastMessage;

    @ManyToOne
    @JoinColumn(name = "first_person_id", nullable = false)
    private Person firstPerson;

    @ManyToOne
    @JoinColumn(name = "second_person_id", nullable = false)
    private Person secondPerson;

    public Dialog(Person firstPerson, Person secondPerson, ZonedDateTime lastActiveTime) {
        this.firstPerson = firstPerson;
        this.secondPerson = secondPerson;
        this.lastActiveTime = lastActiveTime;
    }
}
