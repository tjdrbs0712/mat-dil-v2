package hello.matdil.domain.review.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.review.entity.QReview;
import hello.matdil.domain.review.entity.ReviewSortType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static hello.matdil.global.constant.CursorReviewKey.LAST_RATING;
import static hello.matdil.global.constant.CursorReviewKey.LAST_REVIEW_ID;

@Component
public class RatingAscReviewSortStrategy implements ReviewSortStrategy {

    @Override
    public ReviewSortType getSortType() {
        return ReviewSortType.RATING_ASC;
    }

    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QReview review) {
        return new OrderSpecifier[]{
                review.rating.asc(),
                review.id.asc()
        };
    }

    @Override
    public BooleanExpression buildCursorPredicate(QReview review, Map<String, Object> cursorParams) {
        Integer lastRating = (Integer) cursorParams.get(LAST_RATING);
        Long lastReviewId = (Long) cursorParams.get(LAST_REVIEW_ID);

        if (lastRating == null || lastReviewId == null) return null;

        return review.rating.gt(lastRating)
                .or(review.rating.eq(lastRating).and(review.id.gt(lastReviewId)));
    }
}
