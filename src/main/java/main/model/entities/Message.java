package main.model.entities;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp time;

    @ManyToOne
    @Column(name = "author_id")
    private Person author;

    @ManyToOne
    @Column(name = "recipient_id")
    private Person recipient;

    @Column(name = "message_text", length = 10000)
    private String messageText;

    @Column(name = "read_status")
    private String readStatus;

    @ManyToOne
    @Column(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
