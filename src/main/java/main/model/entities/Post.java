package main.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.enums.PostTypes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "posts", indexes = @Index(name = "post_name_index", columnList = "title"))
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Person author;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "post_text", nullable = false, columnDefinition = "TEXT")
    private String postText;

    @Column (name = "is_blocked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isBlocked;

    @Column (name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    @Column (name = "time_delete")
    private LocalDateTime timeDelete;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany(mappedBy = "posts", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostFile> postFiles;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> postLikes;

    @JsonProperty("type")
    public PostTypes getPostType() {
        return PostTypes.getType(isDeleted, time);
    }
}
