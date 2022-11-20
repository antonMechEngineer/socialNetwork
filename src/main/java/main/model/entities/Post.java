package main.model.entities;

import lombok.Data;
import lombok.ToString;

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

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private Person author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments;

    @ManyToMany(mappedBy = "posts", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PostFile> postFiles;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PostLike> postLikes;
}
