package soialNetworkApp.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListRsPersonRs {

    private Long time;

    private Integer total;

    private List<PersonRs> data;

    private Integer itemPerPage;
}
