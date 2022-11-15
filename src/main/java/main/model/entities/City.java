package main.model.entities;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    private String temp;

    private String clouds;
}
