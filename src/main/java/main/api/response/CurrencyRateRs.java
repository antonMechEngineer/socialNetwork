package main.api.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CurrencyRateRs implements Serializable {
    private static final long serialVersionUID = -4439114469417994311L;
    private String usd;
    private String euro;
}
