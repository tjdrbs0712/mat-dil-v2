package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IdSortStrategy implements SortStrategy{

    @Override
    public OrderSpecifier<?> getOrderSpecifier(QStore store) {
        return store.id.desc();
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Long lastId = (Long) cursorParams.get("lastStoreId");
        return lastId != null ? store.id.lt(lastId) : null;
    }
}
