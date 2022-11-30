package main.api.request;

import lombok.Data;

@Data
public class FindPersonRq {

    private String first_name;
    private String last_name;
    private Integer age_from;
    private Integer age_to;
    private String city;
    private String country;
}
