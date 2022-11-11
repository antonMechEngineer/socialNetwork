package main.config.entities;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "country_id")
    private Long countryId;
    private String temp;
    private String clouds;

}
