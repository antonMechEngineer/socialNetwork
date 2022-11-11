package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptchaRs {
    private String code;
    private String image;
}
