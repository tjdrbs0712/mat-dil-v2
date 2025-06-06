package hello.matdil.domain.review.reviewreply.exception;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.ErrorCode;

public class ReviewReplyException extends BusinessException {
    public ReviewReplyException(ErrorCode errorCode) {
        super(errorCode);
    }
}
