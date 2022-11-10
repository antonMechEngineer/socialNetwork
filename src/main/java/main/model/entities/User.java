package main.model.entities;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "e_Mail")
    private String eMail;
    private String password;
    private String type;
}
