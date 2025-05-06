package hello.matdil.domain.user.exception;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.ErrorCode;

public class UserException extends BusinessException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
