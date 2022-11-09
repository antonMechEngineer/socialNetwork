package main.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListResponseRsPostRs {

    private String error;
    private long timestamp;
    private long total;
    private int offset;
    private List<Post> data;
    private int itemPerPage;
    private String errorDescription;
}
