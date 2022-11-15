package main.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageRs {
    private String error;
    private long timestamp;
    private StorageDataRs data;
}
