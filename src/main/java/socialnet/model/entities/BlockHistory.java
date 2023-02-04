package socialnet.model.entities;

import lombok.Data;
import socialnet.model.enums.BlockActionTypes;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "block_history")
public class BlockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'BLOCK'")
    @Enumerated(EnumType.STRING)
    private BlockActionTypes action;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @OneToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment comment;

    @Override
    public String toString() {
        return "BlockHistory{" +
                "id=" + id +
                ", time=" + time +
                ", action=" + action +
                ", personId=" + person.getId() +
                ", postId=" + (post == null ? "no post" : post.getId()) +
                ", commentId=" + (comment == null ? "no comment" : comment.getId()) +
                '}';
    }
}
