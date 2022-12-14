package main.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "post search data")
public class FindPostRq {

    @ApiModelProperty(value = "text that can contain a post", example = "Hogwarts")
    private String text;

    @ApiModelProperty(value = "post tags", example = "[summer,funny]")
    private List<String> tags;

    @ApiModelProperty(value = "post can be written after this date", example = "111111111")
    private Long date_from;

    @ApiModelProperty(value = "post can be written before this date", example = "111111111")
    private Long date_to;

    @ApiModelProperty(value = "possible post author", example = "Максим Иванов")
    private String author;
}
