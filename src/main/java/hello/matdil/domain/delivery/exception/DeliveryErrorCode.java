package hello.matdil.domain.delivery.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryErrorCode implements ErrorCode {
    DELIVERY_ALREADY_EXISTS(400, "DELIVERY_ALREADY_EXISTS", "이미 배달이 생성된 주문입니다."),


    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}

