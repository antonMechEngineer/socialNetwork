package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentRequest {

    @JsonProperty("parent_id")
    private long parentId;
    @JsonProperty("comment_text")
    private String commentText;
//    @JsonProperty("get_deleted")
//    private boolean getDeleted;
}
