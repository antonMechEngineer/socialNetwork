package main.model.entities;

import lombok.Data;
import lombok.ToString;
import main.model.enums.BlockActionTypes;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "block_history")
public class BlockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'BLOCK'")
    @Enumerated(EnumType.STRING)
    private BlockActionTypes action;

    @ManyToOne
    @JoinColumn(name = "person_id")
    @ToString.Exclude
    private Person person;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @ToString.Exclude
    private Post post;

    @OneToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    @ToString.Exclude
    private Comment comment;
}
