package hello.matdil.domain.review.dto;

import hello.matdil.domain.review.entity.ReviewSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
public class ReviewCursorResponseDto{
    private int size;
    private Object lastValue;
    private Long lastReviewId;

    public static ReviewCursorResponseDto from(int size, ReviewSortType sortType, ReviewResponseDto review){
        Object lastValue = null;

        if (Objects.requireNonNull(sortType) == ReviewSortType.LATEST) {
            lastValue = review.getUpdatedAt();
        } else {
            lastValue = review.getRating();
        }

        return ReviewCursorResponseDto.builder()
                .size(size)
                .lastValue(lastValue)
                .lastReviewId(review.getId())
                .build();
    }

}
