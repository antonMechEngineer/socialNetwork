package main.model.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "dialogs")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_time_active")
    private LocalDateTime lastTimeActive;

    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "first_person_id", nullable = false)
    @ToString.Exclude
    private Person firstPerson;

    @ManyToOne
    @JoinColumn(name = "second_person_id", nullable = false)
    @ToString.Exclude
    private Person secondPerson;
}
