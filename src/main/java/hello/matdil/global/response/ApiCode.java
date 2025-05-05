package hello.matdil.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiCode {
    SUCCESS("SUCCESS", "요청이 성공했습니다.");

    private final String code;
    private final String message;
}