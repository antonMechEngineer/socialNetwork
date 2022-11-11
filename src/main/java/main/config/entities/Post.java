package main.config.entities;
import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @Column(name = "title")
    private String title;

    @Column(name = "post_text", length = 10000)
    private String postText;

    @Column (name = "is_blocked")
    private Boolean isBlocked;

    @Column (name = "is_deleted")
    private Boolean isDeleted;

    @Column (name = "time_delete")
    private Timestamp timeDelete;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostFile> postFiles;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postComments;


}
