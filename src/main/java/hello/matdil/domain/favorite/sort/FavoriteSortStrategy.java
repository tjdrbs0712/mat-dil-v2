package hello.matdil.domain.favorite.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.order.entity.QOrder;
import hello.matdil.domain.store.entity.QStore;

import java.util.List;
import java.util.Map;

public interface FavoriteSortStrategy {
    List<OrderSpecifier<?>> getOrderSpecifiers(QStore store, QOrder order);

    BooleanExpression buildCursorPredicate(QStore store, QOrder order, Map<String, Object> cursorParams);
}
