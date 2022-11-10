package main.model.entities;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Data
@Table(name = "friendship_status")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp time;
    private String name;
    private String code;


}
