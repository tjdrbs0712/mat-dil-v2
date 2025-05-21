package hello.matdil.domain.store.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {
    NO_PERMISSION(403, "NO_PERMISSION", "해당 가게에 대한 권한이 없습니다."),
    STORE_NOT_FOUND(404, "STORE_NOT_FOUND", "해당 가게를 찾을 수 없습니다."),
    INVALID_STORE_TIME(400, "INVALID_STORE_TIME", "영업 종료 시간은 시작 시간보다 늦어야 합니다."),
    STORE_STATUS_UNCHANGED(400, "STORE_STATUS_UNCHANGED", "현재 상태와 동일한 값으로 변경할 수 없습니다."),

    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
