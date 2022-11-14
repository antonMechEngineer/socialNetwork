package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.model.entities.Post;

import java.util.List;

@Data
@AllArgsConstructor
public class PostResponse {

    private String error;
    private long timestamp;
    private int offset;
    private int perPage;
    private List<Post> data;
    private String errorDescription;
}
