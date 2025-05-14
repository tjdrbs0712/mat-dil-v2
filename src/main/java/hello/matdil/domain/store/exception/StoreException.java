package hello.matdil.domain.store.exception;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.ErrorCode;

public class StoreException extends BusinessException {
    public StoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
