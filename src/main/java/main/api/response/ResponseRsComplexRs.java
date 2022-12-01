package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseRsComplexRs {
    private String error;
    private long timestamp;
    private int offset;
    private int perPage;
    ComplexRs data;
    private String error_description;
}
