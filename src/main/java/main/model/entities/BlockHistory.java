package main.model.entities;

import lombok.Data;
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

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @OneToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment comment;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'BLOCK'")
    @Enumerated(EnumType.STRING)
    private BlockActionTypes action;
}
