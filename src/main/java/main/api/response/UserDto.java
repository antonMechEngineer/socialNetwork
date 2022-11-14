package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private long id;
    private String email;
    private String phone;
    private String photo;
    private String about;
    private CityRs city;
    private CountryRs country;
    private String first_name;
    private String last_name;
    private long reg_date;
    private long birth_date;
    private String messages_permission;
    private long last_online_time;
    private boolean is_blocked;
}
