package hello.matdil.domain.delivery.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryErrorCode implements ErrorCode {
    DELIVERY_ALREADY_EXISTS(400, "DELIVERY_ALREADY_EXISTS", "이미 배달이 생성된 주문입니다."),
    DELIVERY_NOT_FOUND(404, "DELIVERY_NOT_FOUND", "존재하지 않는 배달입니다."),
    DELIVERY_NOT_AVAILABLE(400, "DELIVERY_NOT_AVAILABLE", "이미 배정된 배달입니다.")
    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}

