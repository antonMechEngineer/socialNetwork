package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostRequest {

    private String title;
    private List<String> tags;
    @JsonProperty("post_text")
    private String postText;
//    @JsonProperty("get_deleted")
//    private boolean getDeleted;
}
