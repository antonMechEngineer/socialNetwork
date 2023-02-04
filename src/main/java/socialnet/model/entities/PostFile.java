package socialnet.model.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "post_files")
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;

    @Override
    public String toString() {
        return "PostFile{" +
                "id=" + id +
                ", name='" + name +
                "', path='" + path +
                "', postId=" + post.getId() +
                '}';
    }
}
