package hello.matdil.global.exception.business;

import hello.matdil.global.exception.errorcode.ErrorCode;

public class UserException extends BusinessException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
