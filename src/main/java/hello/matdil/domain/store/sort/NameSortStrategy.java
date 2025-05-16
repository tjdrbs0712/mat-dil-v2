package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class NameSortStrategy implements SortStrategy{
    @Override
    public OrderSpecifier<?> getOrderSpecifier(QStore store) {
        return store.name.asc();
    }

    @Override
    public BooleanExpression buildCursorPredicate(QStore store, Map<String, Object> cursorParams) {
        String lastName = (String) cursorParams.get("lastName");
        return StringUtils.hasText(lastName) ? store.name.gt(lastName) : null;
    }
}
