package main.api.response;
import lombok.Data;

import javax.persistence.Entity;

@Data
public class WeatherRs {
    private Long id;
    private String clouds;
    private String temp;
    private String city;
}
