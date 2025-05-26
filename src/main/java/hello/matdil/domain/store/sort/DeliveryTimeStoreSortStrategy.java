package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.global.sort.StoreSortStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

import static hello.matdil.global.util.CursorKey.LAST_DELIVERY_TIME;
import static hello.matdil.global.util.CursorKey.LAST_STORE_ID;

@Component
public class DeliveryTimeStoreSortStrategy implements StoreSortStrategy {
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store) {
        return new OrderSpecifier[]{store.deliveryTimeEstimate.asc(), store.id.asc()};
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Integer lastDeliveryTime = (Integer) cursorParams.get(LAST_DELIVERY_TIME);
        Long lastId = (Long) cursorParams.get(LAST_STORE_ID);
        if (lastDeliveryTime == null || lastId == null) return null;
        return store.deliveryTimeEstimate.gt(lastDeliveryTime)
                .or(store.deliveryTimeEstimate.eq(lastDeliveryTime).and(store.id.gt(lastId)));
    }
}