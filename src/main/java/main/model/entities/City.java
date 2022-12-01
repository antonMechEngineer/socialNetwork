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


    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", title='" + title +
                '}';
    }
}
