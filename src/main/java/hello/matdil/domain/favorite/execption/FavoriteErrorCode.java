package hello.matdil.domain.favorite.execption;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FavoriteErrorCode implements ErrorCode {

    ALREADY_FAVORITE(400, "ALREADY_FAVORITE", "이미 즐겨찾기된 가게입니다."),
    FAVORITE_NOT_FOUND(404, "FAVORITE_NOT_FOUND", "즐겨찾기한 가게가 존재하지 않습니다."),
    ALREADY_IN_TARGET_STATUS(400, "ALREADY_IN_TARGET_STATUS", "이미 변경된 상태입니다."),
    INVALID_SORT_TYPE(400, "INVALID_SORT_TYPE", "지원하지 않는 정렬 타입입니다."),

    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}

