package soialNetworkApp.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String district;

    @Column(name = "sub_district")
    private String subDistrict;

    @Column(name = "gismeteo_id")
    private Integer gismeteoId;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", title='" + name +
                '}';
    }
}
