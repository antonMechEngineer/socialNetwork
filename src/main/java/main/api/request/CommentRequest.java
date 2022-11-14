package main.api.request;

import lombok.Data;

@Data
public class CommentRequest {

    private long parentId;
    private String commentText;
    private boolean getDeleted;
}
