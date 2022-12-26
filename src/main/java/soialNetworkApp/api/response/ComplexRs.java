package soialNetworkApp.api.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplexRs {
    private Integer id;
    private Long count;
    private String message;
    private Long message_id;

    public ComplexRs(Long count) {
        this.count = count;
    }
}
