package soialNetworkApp.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class FriendsRs {

    private Long time;

    private Integer total;

    private List<PersonRs> data;

    private Integer itemPerPage;
}
