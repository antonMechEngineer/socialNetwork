package main.model.entities;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Post2Tag> post2Tag;
}
