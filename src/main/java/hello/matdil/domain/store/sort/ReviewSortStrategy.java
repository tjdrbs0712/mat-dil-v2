package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.global.sort.SortStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

import static hello.matdil.global.util.CursorKey.LAST_REVIEW_COUNT;
import static hello.matdil.global.util.CursorKey.LAST_STORE_ID;

@Component
public class ReviewSortStrategy implements SortStrategy {
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store) {
        return new OrderSpecifier[]{store.reviewCount.desc(), store.id.desc()};
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Integer lastReviewCount = (Integer) cursorParams.get(LAST_REVIEW_COUNT);
        Long lastId = (Long) cursorParams.get(LAST_STORE_ID);
        if (lastReviewCount == null || lastId == null) return null;
        return store.reviewCount.lt(lastReviewCount)
                .or(store.reviewCount.eq(lastReviewCount).and(store.id.lt(lastId)));
    }
}