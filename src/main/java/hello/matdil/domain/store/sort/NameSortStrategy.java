package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.global.sort.SortStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

import static hello.matdil.global.util.CursorKey.LAST_NAME;
import static hello.matdil.global.util.CursorKey.LAST_STORE_ID;

@Component
public class NameSortStrategy implements SortStrategy {
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(QStore store) {
        return new OrderSpecifier[]{store.name.asc(), store.id.asc()};
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        String lastName = (String) cursorParams.get(LAST_NAME);
        Long lastId = (Long) cursorParams.get(LAST_STORE_ID);
        if (!StringUtils.hasText(lastName) || lastId == null) return null;
        return store.name.gt(lastName)
                .or(store.name.eq(lastName).and(store.id.gt(lastId)));
    }
}