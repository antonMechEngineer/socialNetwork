package main.model.entities;

import lombok.Data;
import main.model.enums.LikeTypes;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ManyToAny;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "posts", indexes = @Index(name = "post_name_index", columnList = "title"))
public class Post implements Liked{

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
    @ToString.Exclude
    private List<Comment> comments;

    @ManyToMany(mappedBy = "posts", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PostFile> postFiles;

//    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
//    private List<Like> likes;

//    @ManyToAny(metaDef = "likesMetaDef", metaColumn = @Column(name = "meta_column"))
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    @JoinTable(name = "post_likes", joinColumns = @JoinColumn(name = "entity_id"), inverseJoinColumns = @JoinColumn(name = "ent_id"))
//    private List<Like> likes;


    @Override
    public LikeTypes getType() {
        return LikeTypes.POST;
    }
}
