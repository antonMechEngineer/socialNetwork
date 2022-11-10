package main.model.entities;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String price;
}
