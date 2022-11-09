package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "post_comment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Timestamp time;

    @ManyToOne
    @Column(name = "post_id")
    private Post post;

    @OneToOne
    @Column(name = "parent_id") // TODO: 09.11.2022 не уверен, что правильно выстроена бд, самозамыкание
    private PostComment postComment;

    @ManyToOne
    @Column(name = "author_id")
    private Person person;

    private String password;
    private String type;
}
