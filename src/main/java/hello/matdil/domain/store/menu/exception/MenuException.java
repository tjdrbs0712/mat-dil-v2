package hello.matdil.domain.store.menu.exception;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.ErrorCode;

public class MenuException extends BusinessException {
    public MenuException(ErrorCode errorCode) {
        super(errorCode);
    }
}
