package soialNetworkApp.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities",
        indexes = @Index(name = "full_city_name_index", columnList = "name, district, sub_district", unique = true))
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
