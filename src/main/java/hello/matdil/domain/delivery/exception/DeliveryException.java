package hello.matdil.domain.delivery.exception;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.ErrorCode;

public class DeliveryException extends BusinessException {
    public DeliveryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
