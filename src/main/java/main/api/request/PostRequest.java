package main.api.request;

import lombok.Data;
import main.model.entities.Tag;

import java.util.List;

@Data
public class PostRequest {

    private String title;
    private List<Tag> tags;
    private String postText;
    private boolean getDeleted;
}
