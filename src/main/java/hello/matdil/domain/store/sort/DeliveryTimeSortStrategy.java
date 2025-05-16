package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeliveryTimeSortStrategy implements SortStrategy {
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store) {
        return new OrderSpecifier[]{store.deliveryTimeEstimate.asc(), store.id.asc()};
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Integer lastDeliveryTime = (Integer) cursorParams.get("lastDeliveryTime");
        Long lastId = (Long) cursorParams.get("lastStoreId");
        if (lastDeliveryTime == null || lastId == null) return null;
        return store.deliveryTimeEstimate.gt(lastDeliveryTime)
                .or(store.deliveryTimeEstimate.eq(lastDeliveryTime).and(store.id.gt(lastId)));
    }
}