package main.model.entities;

import lombok.Data;
import main.model.enums.LikeTypes;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

//    @ManyToOne
//    @JoinColumn(name = "post_id", nullable = false)
//    private Post post;

    @Any(metaDef = "likesMetaDef", metaColumn = @Column(name = "type"), fetch = FetchType.EAGER)
    @AnyMetaDef(name = "likesMetaDef", idType = "long", metaType = "string", metaValues = {
            @MetaValue(targetEntity = Post.class, value = "POST"),
            @MetaValue(targetEntity = Comment.class, value = "COMMENT")
    })
    @JoinColumn(name = "entity_id")
    private Object entity;

    @Column(nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private LikeTypes type;
}
