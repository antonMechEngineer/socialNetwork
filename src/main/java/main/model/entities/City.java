package main.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
//@ToString
//@Getter
//@Setter
//@RequiredArgsConstructor
//@EqualsAndHashCode

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
