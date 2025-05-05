package hello.matdil.global.exception.business;

import hello.matdil.global.exception.errocode.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
//        super(errorCode.getErrorMessage()); // 기본 메시지
        this.errorCode = errorCode;
    }
}
