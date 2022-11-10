package main.model.entities;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "post_file")
public class PostFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private long name;
    private long path;
}
