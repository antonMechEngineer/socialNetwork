package main.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Timestamp time;

    @Column(name = "author_id")
    private long authorID;

    @Column(name = "recipient_id")
    private long recipientID;

    @Column(name = "second_person_id")
    private long secondPersonID;

    @Column(name = "message_text", length = 10000)
    private String messageText;

    @Column(name = "read_status")
    private String readStatus;

    @Column(name = "dialog_id")
    private long dialogID;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
