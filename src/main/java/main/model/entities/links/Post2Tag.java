package main.model.entities.links;
import lombok.Data;
import main.model.entities.Post;
import main.model.entities.Tag;

import javax.persistence.*;
@Entity
@Data
@Table(name = "post2tag", indexes = @Index(name = "post2tag_index", columnList = "tag_id, post_id", unique = true))
public class Post2Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
}
