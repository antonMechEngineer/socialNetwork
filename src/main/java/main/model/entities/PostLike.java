package main.model.entities;

import lombok.Data;
import lombok.ToString;
import main.model.enums.PostLikeTypes;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "post_likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostLikeTypes type;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @ToString.Exclude
    private Person person;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;
}
