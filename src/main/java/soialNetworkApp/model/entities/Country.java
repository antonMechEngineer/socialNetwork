package soialNetworkApp.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "international_name")
    private String internationalName;

    @Column(name = "code2")
    private String codeTwoSymbols;

    @OneToMany(mappedBy = "country")
    private List<City> cities;
}
