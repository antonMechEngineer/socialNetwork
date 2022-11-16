package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipRs {
    String error;
    String timestamp;
    ComplexRs data;
}
