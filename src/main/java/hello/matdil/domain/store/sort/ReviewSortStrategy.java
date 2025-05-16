package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReviewSortStrategy implements SortStrategy{
    @Override
    public OrderSpecifier<?> getOrderSpecifier(QStore store) {
        return store.reviewCount.desc();
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Integer lastReviewCount = (Integer) cursorParams.get("lastReviewCount");
        return lastReviewCount != null ? store.reviewCount.lt(lastReviewCount) : null;
    }
}
