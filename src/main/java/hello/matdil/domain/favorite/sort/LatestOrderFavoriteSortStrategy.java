package hello.matdil.domain.favorite.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.favorite.entity.FavoriteSortType;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.userorderstatus.entity.QUserOrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class LatestOrderFavoriteSortStrategy implements FavoriteSortStrategy {

    @Override
    public FavoriteSortType getSortType() {
        return FavoriteSortType.LATEST_ORDER;
    }

    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store, QUserOrderStatus status) {
        return new OrderSpecifier[]{
                status.lastOrderedAt.desc().nullsLast(),
                store.id.desc()
        };
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, QUserOrderStatus status, Map<String, Object> cursorParams) {
        LocalDateTime lastOrderedAt = (LocalDateTime) cursorParams.get("lastOrderedAt");
        Long lastStoreId = (Long) cursorParams.get("lastStoreId");
        if (lastOrderedAt == null || lastStoreId == null) return null;

        return status.lastOrderedAt.before(lastOrderedAt)
                .or(status.lastOrderedAt.eq(lastOrderedAt).and(store.id.lt(lastStoreId)));
    }
}
