package hello.matdil.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    SUCCESS("SUCCESS", "요청이 성공했습니다."),
    EMAIL_VERIFICATION_SUCCESS("EMAIL_VERIFICATION_SUCCESS", "이메일 인증이 완료되었습니다.");

    private final String code;
    private final String message;
}