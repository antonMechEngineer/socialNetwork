package main.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherRs {

    private String clouds;
    private String temp;
    private String city;
}
