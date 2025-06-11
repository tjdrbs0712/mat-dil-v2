package hello.matdil.domain.payment.dto.portone;

public record PortonePaymentResponseWrapper(
        int code,
        String message,
        PortonePaymentData response
) {}