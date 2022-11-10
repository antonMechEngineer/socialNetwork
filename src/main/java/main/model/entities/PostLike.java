package main.model.entities;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "post_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "post_id")
    private Long postID;

    private String type;

}
