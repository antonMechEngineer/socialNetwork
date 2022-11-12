package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.model.entities.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentsListResponse {

    private String error;
    private LocalDateTime time;
    private int page;
    private int size;
    private List<Comment> data;
    private String errorDescription;
}
