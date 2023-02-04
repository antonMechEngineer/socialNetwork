package socialnet.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegionStatisticRs {

    private String region;

    private Long countUsers;
}
