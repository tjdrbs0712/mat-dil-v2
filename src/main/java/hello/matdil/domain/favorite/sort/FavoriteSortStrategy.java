package hello.matdil.domain.favorite.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.favorite.entity.FavoriteSortType;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.userorderstatus.entity.QUserOrderStatus;

import java.util.Map;

public interface FavoriteSortStrategy {
    FavoriteSortType getSortType();
    OrderSpecifier<?>[] getOrderSpecifiers(QStore store, QUserOrderStatus status);
    BooleanExpression buildCursorPredicate(QStore store, QUserOrderStatus status, Map<String, Object> cursorParams);
}
