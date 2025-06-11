package hello.matdil.domain.payment.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {
    NO_PERMISSION(403, "NO_PERMISSION", "해당 결제에 대한 권한이 없습니다."),
    PAYMENT_NOT_FOUND(404, "PAYMENT_NOT_FOUND", "결제 정보를 찾을 수 없습니다."),
    INVALID_AMOUNT(400, "INVALID_AMOUNT", "결제 금액이 일치하지 않습니다. 위변조 시도가 의심됩니다."),
    ALREADY_COMPLETED(400, "ALREADY_COMPLETED", "이미 결제 완료된 주문입니다."),
    PORTONE_NOT_FOUND(502, "PORTONE_NOT_FOUND", "포트원에서 결제 정보를 조회할 수 없습니다."),
    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}