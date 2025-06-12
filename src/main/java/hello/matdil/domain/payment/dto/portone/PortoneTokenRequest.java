package hello.matdil.domain.payment.dto.portone;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PortoneTokenRequest(
        @JsonProperty("imp_key") String apiKey,
        @JsonProperty("imp_secret") String secretKey
) {}