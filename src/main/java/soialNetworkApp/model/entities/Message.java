package soialNetworkApp.model.entities;

import lombok.*;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.NotificationTypes;
import soialNetworkApp.model.enums.ReadStatusTypes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message implements Notificationed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime time;

    @Column(name = "message_text", nullable = false, columnDefinition = "TEXT")
    private String messageText;

    @Column(name = "read_status", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'SENT'")
    @Enumerated(EnumType.STRING)
    private ReadStatusTypes readStatus;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Person author;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Person recipient;

    @ManyToOne
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;

    @Override
    public NotificationTypes getNotificationType() {
        return NotificationTypes.MESSAGE;
    }
}
