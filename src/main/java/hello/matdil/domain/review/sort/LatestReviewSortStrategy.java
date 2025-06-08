package hello.matdil.domain.review.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.review.entity.QReview;
import hello.matdil.domain.review.entity.ReviewSortType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static hello.matdil.global.constant.CursorReviewKey.LAST_REVIEW_ID;
import static hello.matdil.global.constant.CursorReviewKey.LAST_UPDATED_AT;

@Component
public class LatestReviewSortStrategy implements ReviewSortStrategy {

    @Override
    public ReviewSortType getSortType() {
        return ReviewSortType.LATEST;
    }

    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QReview review) {
        return new OrderSpecifier[]{
                review.updatedAt.desc(),
                review.id.desc()
        };
    }

    @Override
    public BooleanExpression buildCursorPredicate(QReview review, Map<String, Object> cursorParams) {
        LocalDateTime lastCreatedAt = (LocalDateTime) cursorParams.get(LAST_UPDATED_AT);
        Long lastReviewId = (Long) cursorParams.get(LAST_REVIEW_ID);

        if (lastCreatedAt == null || lastReviewId == null) return null;

        return review.createdAt.lt(lastCreatedAt)
                .or(review.createdAt.eq(lastCreatedAt).and(review.id.lt(lastReviewId)));
    }
}
