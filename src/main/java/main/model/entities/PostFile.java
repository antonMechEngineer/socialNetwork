package main.model.entities;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "post_files")
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;
}
