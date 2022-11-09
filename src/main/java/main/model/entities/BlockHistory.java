package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    @Column(name = "person_id")
    private long personID;
    @Column(name = "post_id")
    private long postID;
    @Column(name = "comment_id")
    private long commentID;
    private String action;
}
