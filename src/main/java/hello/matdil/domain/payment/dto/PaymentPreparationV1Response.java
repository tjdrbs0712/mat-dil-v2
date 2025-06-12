package hello.matdil.domain.payment.dto;

import hello.matdil.domain.payment.entity.Payment;

import java.math.BigDecimal;

public record PaymentPreparationV1Response(
        String merchant_uid,
        BigDecimal amount,
        String pg
) {
    public static PaymentPreparationV1Response from(Payment payment, String pgProvider) {
        return new PaymentPreparationV1Response(
                String.valueOf(payment.getId()),
                payment.getAmount(),
                pgProvider
        );
    }
}