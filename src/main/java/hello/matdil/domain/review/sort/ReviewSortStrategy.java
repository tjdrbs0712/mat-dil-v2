package hello.matdil.domain.review.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.review.entity.QReview;
import hello.matdil.domain.review.entity.ReviewSortType;

import java.util.Map;

public interface ReviewSortStrategy {
    ReviewSortType getSortType();
    OrderSpecifier<?>[] getOrderSpecifiers(QReview review);
    BooleanExpression buildCursorPredicate(QReview review, Map<String, Object> cursorParams);
}
