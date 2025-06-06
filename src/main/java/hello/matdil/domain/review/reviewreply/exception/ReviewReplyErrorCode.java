package hello.matdil.domain.review.reviewreply.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewReplyErrorCode implements ErrorCode {
    ALREADY_REVIEW_REPLY(400, "ALREADY_REVIEW", "이미 등록된 리뷰 답글 입니다."),
    REVIEW_REPLY_NOT_FOUND(404, "REVIEW_NOT_FOUND", "등록된 리뷰 답글이 없습니다."),
    NO_PERMISSION(403, "NO_PERMISSION", "리뷰 답글을 수정할 권한이 없습니다."),

    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
