package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "post_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Timestamp time;

    @ManyToOne
    @Column(name = "person_id")
    private Person person;

    @Column(name = "post_id")
    private long postID;

    private String type;

}
