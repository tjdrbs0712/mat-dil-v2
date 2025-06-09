package hello.matdil.domain.payment.dto;

public record PaymentPreparationRequest(
        Long orderId,
        String pg,
        String method
) {}