package hello.matdil.domain.favorite.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.favorite.entity.FavoriteSortType;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.userorderstatus.entity.QUserOrderStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderCountFavoriteSortStrategy implements FavoriteSortStrategy {

    @Override
    public FavoriteSortType getSortType() {
        return FavoriteSortType.MOST_ORDERED;
    }

    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store, QUserOrderStatus status) {
        return new OrderSpecifier[]{
                status.orderCount.desc().nullsLast(),
                store.id.desc()
        };
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, QUserOrderStatus status, Map<String, Object> cursorParams) {
        Integer lastOrderCount = (Integer) cursorParams.get("lastOrderCount");
        Long lastStoreId = (Long) cursorParams.get("lastStoreId");
        if (lastOrderCount == null || lastStoreId == null) return null;

        return status.orderCount.lt(lastOrderCount)
                .or(status.orderCount.eq(lastOrderCount).and(store.id.lt(lastStoreId)));
    }
}
