package main.model.entities;
import lombok.Data;
import javax.persistence.*;
@Entity
@Data
@Table(name = "post2tag")
public class Post2Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "tag_id")
    private Tag tag;

    @Column(name = "post_id")
    private Long postID;

}
