package hello.matdil.domain.review.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    ALREADY_REVIEW(400, "ALREADY_REVIEW", "이미 등록된 리뷰입니다."),

    ;


    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
