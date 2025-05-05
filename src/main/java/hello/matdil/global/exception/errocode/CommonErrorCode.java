package hello.matdil.global.exception.errocode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    INVALID_REQUEST(400, "INVALID_REQUEST", "요청이 잘못되었습니다."),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
