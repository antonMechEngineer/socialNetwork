package main.model.entities;
import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "post_comment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp time;

    @ManyToOne
    @Column(name = "post_id")
    private Post post;

    @OneToOne
    @Column(name = "parent_id")
    private PostComment postComment; // TODO: 10.11.2022 самозамыкание!

    @ManyToOne
    @Column(name = "author_id")
    private Person person;

    private String password;
    private String type;
}
