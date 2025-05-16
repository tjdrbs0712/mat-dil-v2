package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RatingSortStrategy implements SortStrategy {

    @Override
    public OrderSpecifier<?> getOrderSpecifier(QStore store) {
        return store.rating.desc();
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Double lastRating = (Double) cursorParams.get("lastRating");
        return lastRating != null ? store.rating.lt(lastRating) : null;
    }
}