package hello.matdil.domain.review.exception;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.ErrorCode;

public class ReviewException extends BusinessException {
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
