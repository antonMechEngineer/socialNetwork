package soialNetworkApp.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String price;
}
