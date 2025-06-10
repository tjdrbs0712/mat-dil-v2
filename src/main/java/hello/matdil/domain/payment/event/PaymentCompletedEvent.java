package hello.matdil.domain.payment.event;

import hello.matdil.domain.payment.entity.Payment;

public record PaymentCompletedEvent(
        Long orderId,
        Long paymentId
) {
    public static PaymentCompletedEvent from(Payment payment) {
        return new PaymentCompletedEvent(payment.getOrderId(), payment.getId());
    }
}
