package hello.matdil.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{
    EMAIL_DUPLICATION(404, "EMAIL_DUPLICATION", "중복된 이메일 입니다."),
    PHONE_DUPLICATION(404, "PHONE_DUPLICATION", "중복된 전화번호 입니다.")

    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
