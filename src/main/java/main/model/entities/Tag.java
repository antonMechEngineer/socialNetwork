package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tag;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Post2Tag> post2Tag;
}
