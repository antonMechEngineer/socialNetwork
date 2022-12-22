package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class WeatherRs implements Serializable {
    private static final long serialVersionUID = -4439114469417994311L;
    private String clouds;
    private String temp;
    private String city;
}
