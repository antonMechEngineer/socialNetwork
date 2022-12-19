package soialNetworkApp.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(indexes = @Index(name = "gismeteo_id_index", columnList = "gismeteo_id"))
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gismeteo_id", nullable = false)
    private Integer gismeteoId;

    @Column(nullable = false)
    private Double temperature;

    @Column(name = "description", nullable = false)
    private String weatherDescription;

    @Column(nullable = false)
    private LocalDateTime time;
}
