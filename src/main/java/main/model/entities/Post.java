package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
@Entity
@Getter
@Setter
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Timestamp time;
    @Column(name = "author_id")
    private long authorID;
    @Column(name = "title")
    private String title;
    @Column(name = "post_text", length = 10000)
    private String postText;
    @Column (name = "is_blocked")
    private boolean isBlocked;
    @Column (name = "is_deleted")
    private boolean isDeleted;
    @Column (name = "time_delete")
    private Timestamp timeDelete;
}
