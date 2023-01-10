package soialNetworkApp.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@ApiModel(description = "default response from server")
public class CommonRs<T> {

    @ApiModelProperty(value = "operation time in timestamp", required = true, example = "1670773804")
    private Long timestamp = System.currentTimeMillis();

    @ApiModelProperty(value = "page number", example = "0")
    private Integer offset;

    @ApiModelProperty(value = "number of elements per page", example = "20")
    private Integer perPage;

    @ApiModelProperty(value = "number of elements per page", example = "20")
    private Integer itemPerPage;

    @ApiModelProperty(value = "total number of items found", example = "500")
    private Long total;

    @ApiModelProperty(value = "any type. The data we are looking for", example = "Collection of objects or just object")
    private T data;

    @Override
    public String toString() {
        return "CommonRs{" +
                "data=" + data +
                '}';
    }

    public CommonRs(T data) {
        this.data = data;
    }

    public CommonRs(T data, Long total) {
        this.data = data;
        this.total = total;
    }
}
