package main.api.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplexRs {
    private Integer id;
    private Integer count;
    private String message;
    private Long message_id;
}
