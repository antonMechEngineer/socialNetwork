package main.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "dialogs")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_person_id", nullable = false)
    private Person firstPerson;

    @ManyToOne
    @JoinColumn(name = "second_person_id", nullable = false)
    private Person secondPerson;

    @Column(name = "last_time_active")
    private LocalDateTime lastTimeActive;

    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL)
    private List<Message> messages;
}
