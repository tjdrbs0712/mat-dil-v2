package hello.matdil.domain.payment.exception;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.ErrorCode;

public class PaymentException extends BusinessException {
    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
