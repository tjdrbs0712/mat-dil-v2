package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RatingSortStrategy implements SortStrategy {
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store) {
        return new OrderSpecifier[]{store.rating.desc(), store.id.desc()};
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Double lastRating = (Double) cursorParams.get("lastRating");
        Long lastId = (Long) cursorParams.get("lastStoreId");
        if (lastRating == null || lastId == null) return null;
        return store.rating.lt(lastRating)
                .or(store.rating.eq(lastRating).and(store.id.lt(lastId)));
    }
}