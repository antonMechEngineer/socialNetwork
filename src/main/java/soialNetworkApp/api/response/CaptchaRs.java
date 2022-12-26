package soialNetworkApp.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "captcha response")
public class CaptchaRs {

    @ApiModelProperty(value = "secret code for decrypt captcha", required = true, example = "uuid")
    private String code;

    @ApiModelProperty(value = "url of captcha image", required = true, example = "/some/path")
    private String image;
}
