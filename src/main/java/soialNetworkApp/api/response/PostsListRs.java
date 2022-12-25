package soialNetworkApp.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import soialNetworkApp.model.entities.Post;

import java.util.List;

@Data
@AllArgsConstructor
public class PostsListRs {
    private String error;
    private long timestamp;
    private long total;
    private int offset;
    private List<Post> data;
    private int itemPerPage;
    private String errorDescription;
}
