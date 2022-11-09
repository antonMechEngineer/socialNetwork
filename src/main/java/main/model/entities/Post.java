package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Timestamp time;

    @ManyToOne
    @Column(name = "author_id")
    private Person author;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostFile> postFiles;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postComments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<BlockHistory> blockHistories; // TODO: 09.11.2022 не уверен в связи, не очень понимаю что такое block_history


}
