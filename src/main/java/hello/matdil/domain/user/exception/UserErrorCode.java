package hello.matdil.domain.user.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    EMAIL_DUPLICATION(404, "EMAIL_DUPLICATION", "중복된 이메일 입니다."),
    PHONE_DUPLICATION(404, "PHONE_DUPLICATION", "중복된 전화번호 입니다."),
    AUTHENTICATION_FAILED(401, "AUTHENTICATION_FAILED", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_ACCESS_TOKEN(401, "INVALID_ACCESS_TOKEN", "유효하지 않은 토큰 입니다."),
    INVALID_REFRESH_TOKEN(401, "INVALID_REFRESH_TOKEN", "유효하지 않거나 만료된 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "REFRESH_TOKEN_NOT_FOUND", "저장된 리프레시 토큰이 없습니다."),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "존재하지 않는 유저 입니다."),
    INVALID_CURRENT_PASSWORD(401, "INVALID_CURRENT_PASSWORD", "비밀번호가 올바르지 않습니다."),
    SAME_AS_OLD_PASSWORD(400, "SAME_AS_OLD_PASSWORD", "이전과 동일한 비밀번호 입니다."),
    WITHDRAWN_USER(403, "WITHDRAWN_USER", "회원탈퇴 계정입니다."),
    BANNED_USER(403, "BANNED_USER", "차단당한 계정입니다."),
    INVALID_EMAIL_TOKEN(401, "INVALID_ACCESS_TOKEN", "유호하지 않은 이메일 토큰 입니다.")


    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
