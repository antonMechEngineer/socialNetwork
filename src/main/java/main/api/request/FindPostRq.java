package main.api.request;

import lombok.Data;

import java.util.List;

@Data
public class FindPostRq {

    private String text;
    private List<String> tags;
    private Long date_from;
    private Long date_to;
    private String author;
}
