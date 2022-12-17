package soialNetworkApp.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageRs {

    private long timestamp;

    private StorageDataRs data;

}
