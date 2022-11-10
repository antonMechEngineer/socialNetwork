package main.model.entities;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "captcha")
public class Captcha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp time;
    private String code;
    @Column(name = "secret_code")
    private String secretCode;

}
