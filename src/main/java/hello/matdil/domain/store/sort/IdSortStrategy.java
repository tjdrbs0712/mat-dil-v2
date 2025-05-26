package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.global.sort.SortStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

import static hello.matdil.global.util.CursorKey.LAST_STORE_ID;

@Component
public class IdSortStrategy implements SortStrategy {
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store) {
        return new OrderSpecifier[]{store.id.desc()};
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Long lastId = (Long) cursorParams.get(LAST_STORE_ID);
        if (lastId == null) return null;
        return store.id.lt(lastId);
    }
}
