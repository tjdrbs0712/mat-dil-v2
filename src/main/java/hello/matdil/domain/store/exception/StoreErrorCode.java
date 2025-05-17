package hello.matdil.domain.store.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {
    NO_PERMISSION(403, "NO_PERMISSION", "해당 요청에 대한 권한이 없습니다."),
    STORE_NOT_FOUND(404, "STORE_NOT_FOUND", "해당 가게를 찾을 수 없습니다."),


    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
