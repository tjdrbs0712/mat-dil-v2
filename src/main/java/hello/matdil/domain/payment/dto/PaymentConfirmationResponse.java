package hello.matdil.domain.payment.dto;

import hello.matdil.domain.payment.entity.Payment;

import java.math.BigDecimal;

public record PaymentConfirmationResponse(
        Long paymentId,
        Long orderId,
        String status,
        BigDecimal amount
)
{
    public static PaymentConfirmationResponse from(Payment payment) {
        return new PaymentConfirmationResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getStatus().name(),
                payment.getAmount()
        );
    }
}
