package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;

import java.util.Map;

public interface SortStrategy {
    OrderSpecifier<?> getOrderSpecifier(QStore store);
    BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams);
}
