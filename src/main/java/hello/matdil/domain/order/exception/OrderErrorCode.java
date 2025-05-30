package hello.matdil.domain.order.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND(404, "ORDER_NOT_FOUND", "해당 주문을 찾을 수 없습니다."),
    NO_PERMISSION(403, "ORDER_NOT_FOUND", "해당 주문의 대한 권한이 없습니다."),
    INVALID_STATUS(400, "INVALID_STATUS", "해당 상태로 변경할 수 없습니다."),
    ALREADY_IN_TARGET_STATUS(400, "ALREADY_IN_TARGET_STATUS", "이미 변경된 상태입니다."),
    ORDER_NOT_COMPLETED(400, "ORDER_NOT_COMPLETED", "아직 주문 배달인 완료되지 않았습니다.")
    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
