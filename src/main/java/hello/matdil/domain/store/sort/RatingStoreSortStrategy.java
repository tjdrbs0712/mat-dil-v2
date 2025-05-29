package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.springframework.stereotype.Component;

import java.util.Map;

import static hello.matdil.global.constant.CursorStoreKey.LAST_RATING;
import static hello.matdil.global.constant.CursorStoreKey.LAST_STORE_ID;

@Component
public class RatingStoreSortStrategy implements StoreSortStrategy {
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store) {
        return new OrderSpecifier[]{store.rating.desc(), store.id.desc()};
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Double lastRating = (Double) cursorParams.get(LAST_RATING);
        Long lastId = (Long) cursorParams.get(LAST_STORE_ID);
        if (lastRating == null || lastId == null) return null;
        return store.rating.lt(lastRating)
                .or(store.rating.eq(lastRating).and(store.id.lt(lastId)));
    }
}