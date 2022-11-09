package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.stream.events.Comment;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "block_history")
public class BlockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Timestamp time;

    @ManyToOne
    @Column(name = "person_id")
    private Person person;

    @ManyToOne   // TODO: 09.11.2022 не уверен в связи, не очень понимаю что такое block_history
    @Column(name = "post_id")
    private Post post;

    @ManyToOne // TODO: 09.11.2022 не уверен в связи, не очень понимаю что такое block_history
    @Column(name = "comment_id")
    private Comment comment;


    private String action;
}
