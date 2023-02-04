package socialnet.aop.logging.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoadPathDto {

    private String href;
    private String operation_id;
    private String method;
    private String templated;

}
