package soialNetworkApp.model.entities;

import lombok.Data;
import soialNetworkApp.model.entities.interfaces.Liked;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.LikeTypes;
import soialNetworkApp.model.enums.NotificationTypes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Table(name = "posts", indexes = @Index(name = "post_name_index", columnList = "title"))
public class Post implements Liked, Notificationed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Person author;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "post_text", nullable = false, columnDefinition = "TEXT")
    private String postText;

    @Column(name = "is_blocked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isBlocked;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    @Column(name = "time_delete")
    private LocalDateTime timeDelete;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "posts", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostFile> postFiles = new ArrayList<>();

    @Override
    public LikeTypes getType() {
        return LikeTypes.POST;
    }

    @Override
    public NotificationTypes getNotificationType() {
        return NotificationTypes.POST;
    }

    @Override
    public String getSimpleInfo() {
        return title;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", time=" + time +
                ", authorId=" + author.getId() +
                ", title='" + title +
                "', isBlocked=" + isBlocked +
                ", isDeleted=" + isDeleted +
                ", commentsCount=" + comments.size() +
                ", tags=" + Arrays.toString(tags.stream().map(Tag::getTagName).toArray()) +
                ", postFilesCount=" + postFiles.size() +
                '}';
    }
}
