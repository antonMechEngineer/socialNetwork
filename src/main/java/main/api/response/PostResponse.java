package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import main.model.enums.PostTypes;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostResponse {

    private Long id;

    private LocalDateTime time;

    private PersonResponse author;

    private String title;

    private Integer likes;

    private List<String> tags;

    private List<CommentResponse> comments;

    private PostTypes type;

    @JsonProperty("post_text")
    private String postText;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    private Boolean myLike;
}
