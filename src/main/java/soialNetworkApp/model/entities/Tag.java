package soialNetworkApp.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tags", indexes = @Index(name = "tag_index", columnList = "tag", unique = true))
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag", nullable = false)
    private String tagName;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "post2tag", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> posts = new ArrayList<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tagName='" + tagName +
                "', posts=" + posts.size() +
                '}';
    }
}
