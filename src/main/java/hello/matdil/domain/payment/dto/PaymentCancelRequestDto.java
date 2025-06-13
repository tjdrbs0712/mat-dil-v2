package hello.matdil.domain.payment.dto;

public record PaymentCancelRequestDto(
        String impUid,
        String reason
) {
}
