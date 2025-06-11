package hello.matdil.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentConfirmationRequest(
        @JsonProperty("imp_uid")
        String impUid,

        @JsonProperty("merchant_uid")
        Long merchantUid
) { }