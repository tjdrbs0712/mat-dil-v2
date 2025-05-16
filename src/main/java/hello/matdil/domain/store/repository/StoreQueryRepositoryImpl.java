package hello.matdil.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreSortType;
import hello.matdil.domain.store.sort.SortStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final Map<StoreSortType, SortStrategy> sortStrategyMap;

    @Override
    public Slice<Store> findStoresByCondition(String address, String name, String sort, int size, Map<String, Object> cursorParams) {
        QStore store = QStore.store;

        BooleanBuilder builder = buildWhere(address, name, store);

        StoreSortType sortType = StoreSortType.from(sort);
        SortStrategy strategy = sortStrategyMap.get(sortType);

        OrderSpecifier<?> sortCondition = strategy.getOrderSpecifier(store);
        BooleanExpression cursorPredicate = strategy.buildCursorPredicate(store, cursorParams);
        if (cursorPredicate != null) {
            builder.and(cursorPredicate);
        }

        List<Store> result = queryFactory
                .selectFrom(store)
                .where(builder)
                .orderBy(sortCondition)
                .limit(size + 1)
                .fetch();

        boolean hasNext = result.size() > size;
        if (hasNext) result.remove(size);

        return new SliceImpl<>(result, PageRequest.of(0, size), hasNext);
    }

    private BooleanBuilder buildWhere(String address, String name, QStore store) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(address)) {
            builder.and(store.address.city.containsIgnoreCase(address)
                    .or(store.address.street.containsIgnoreCase(address)));
        }

        if (StringUtils.hasText(name)) {
            builder.and(store.name.containsIgnoreCase(name));
        }

        return builder;
    }
}

