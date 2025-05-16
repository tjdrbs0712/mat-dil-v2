package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeliveryTimeSortStrategy implements SortStrategy{
    @Override
    public OrderSpecifier<?> getOrderSpecifier(QStore store) {
        return store.deliveryTimeEstimate.asc();
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        Integer lastTime = (Integer) cursorParams.get("lastDeliveryTime");
        return lastTime != null ? store.deliveryTimeEstimate.gt(lastTime) : null;
    }
}
