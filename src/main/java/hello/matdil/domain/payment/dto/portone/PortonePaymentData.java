package hello.matdil.domain.payment.dto.portone;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PortonePaymentData(
        @JsonProperty("imp_uid")
        String impUid,

        @JsonProperty("merchant_uid")
        String merchantUid,

        BigDecimal amount,
        String status
) {}