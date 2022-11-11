package main.config.entities;
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
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private PostComment postComment; // TODO: 10.11.2022 самозамыкание!

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person person;

    private String password;
    private String type;
}
