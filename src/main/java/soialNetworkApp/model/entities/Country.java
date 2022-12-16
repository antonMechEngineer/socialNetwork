package soialNetworkApp.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
}
