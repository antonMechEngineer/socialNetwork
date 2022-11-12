package main.model.entities;

import lombok.Data;
import main.model.enums.FriendshipStatusTypes;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "friendship_statuses")
public class FriendshipStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendshipStatusTypes name;

    @Column(nullable = false)
    private String code;
}
