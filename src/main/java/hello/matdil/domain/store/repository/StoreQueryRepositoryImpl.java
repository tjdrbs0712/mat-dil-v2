package hello.matdil.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.store.dto.StoreSearchRequestDto;
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
    public Slice<Store> findStoresByCondition(StoreSearchRequestDto request) {
        QStore store = QStore.store;

        BooleanBuilder builder = buildWhere(request.getAddress(), request.getName(), store);

        StoreSortType sortType = StoreSortType.from(request.getSort());
        SortStrategy strategy = sortStrategyMap.get(sortType);

        OrderSpecifier<?> sortCondition = strategy.getOrderSpecifier(store);
        BooleanExpression cursorPredicate = strategy.buildCursorPredicate(store, request.toCursorParamMap());
        if (cursorPredicate != null) {
            builder.and(cursorPredicate);
        }

        List<Store> result = queryFactory
                .selectFrom(store)
                .where(builder)
                .orderBy(sortCondition)
                .limit(request.getSize() + 1)
                .fetch();

        boolean hasNext = result.size() > request.getSize();
        if (hasNext) result.remove(request.getSize());

        return new SliceImpl<>(result, PageRequest.of(0, request.getSize()), hasNext);
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


