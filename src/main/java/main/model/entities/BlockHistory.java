package main.model.entities;
import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "block_history")
public class BlockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "post_id")
    private Long postId;


    @Column(name = "comment_id")
    private Long commentId;


    private String action;
}
