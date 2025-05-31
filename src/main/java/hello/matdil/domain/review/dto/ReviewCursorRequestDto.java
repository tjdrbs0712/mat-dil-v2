package hello.matdil.domain.review.dto;

import hello.matdil.domain.review.entity.ReviewSortType;
import hello.matdil.global.util.CursorUtils;
import hello.matdil.global.validator.EnumValid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static hello.matdil.global.constant.CursorReviewKey.*;

public record ReviewCursorRequestDto(
        Integer size,

        @Min(value = 0, message = "별점은 0 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5 이하여야 합니다.")
        Integer lastRating,

        LocalDateTime lastCreatedAt,

        @Min(value = 1, message = "lastReviewId는 1 이상이어야 합니다.")
        Long lastReviewId,

        @EnumValid(message = "정렬 기준을 제대로 입력해주세요.", enumClass = ReviewSortType.class)
        String sort
) {

    public int pageSize() {
        return CursorUtils.safePageSize(size, 30, 100);
    }

    public boolean hasCursor() {
        return lastCreatedAt != null && lastReviewId != null;
    }

    public ReviewSortType getSortType() {
        return sort != null ? ReviewSortType.valueOf(sort.toUpperCase()) : ReviewSortType.LATEST;
    }

    public int getLastRatingSafe() {
        if (lastRating != null) {
            return lastRating;
        }

        return switch (getSortType()) {
            case RATING_DESC -> 5;
            case RATING_ASC -> 1;
            default -> -1;
        };
    }

    public Map<String, Object> toCursorParamMap() {
        Map<String, Object> cursor = new HashMap<>();
        ReviewSortType sortType = getSortType();

        switch (sortType) {
            case RATING_DESC, RATING_ASC -> {
                cursor.put(LAST_RATING, getLastRatingSafe());
                cursor.put(LAST_REVIEW_ID, lastReviewId);
            }
            case LATEST -> {
                cursor.put(LAST_UPDATED_AT, lastCreatedAt);
                cursor.put(LAST_REVIEW_ID, lastReviewId);
            }
        }
        return cursor;
    }

}
