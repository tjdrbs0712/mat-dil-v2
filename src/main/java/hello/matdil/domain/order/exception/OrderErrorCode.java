package hello.matdil.domain.order.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND(404, "ORDER_NOT_FOUND", "해당 주문을 찾을 수 없습니다."),
    NO_PERMISSION(403, "ORDER_NOT_FOUND", "해당 주문의 대한 권한이 없습니다."),
    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
