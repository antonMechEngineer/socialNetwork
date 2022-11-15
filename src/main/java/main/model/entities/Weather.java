package main.model.entities;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Weather {

    private Long id;
    private String clouds;
    private String temp;
    private String city;
}
