package main.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import main.model.enums.FriendshipStatusTypes;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Data
@Table(name = "friendship_statuses")
public class FriendshipStatus {

    public FriendshipStatus(LocalDateTime time, FriendshipStatusTypes code) {
        this.time = time;
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendshipStatusTypes code;
}
