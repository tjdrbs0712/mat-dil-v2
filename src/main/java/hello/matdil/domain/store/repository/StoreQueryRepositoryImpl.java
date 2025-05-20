package hello.matdil.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.store.dto.StoreSearchRequestDto;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreSortType;
import hello.matdil.domain.store.query.StorePredicateBuilder;
import hello.matdil.domain.store.sort.SortStrategy;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final Map<StoreSortType, SortStrategy> sortStrategyMap;

    @Override
    public List<Store> findStoresByCondition(Long userId, UserRole role, StoreSearchRequestDto request) {
        QStore store = QStore.store;

        BooleanBuilder builder = StorePredicateBuilder.build(
                userId, role, request.getAddress(), request.getName(), store
        );

        StoreSortType sortType = StoreSortType.from(request.getSort());
        SortStrategy strategy = sortStrategyMap.get(sortType);

        OrderSpecifier<?>[] sortConditions = strategy.getOrderSpecifiers(store);
        BooleanExpression cursorPredicate = strategy.buildCursorPredicate(store, request.toCursorParamMap());
        if (cursorPredicate != null) {
            builder.and(cursorPredicate);
        }

        return queryFactory
                .selectFrom(store)
                .where(builder)
                .orderBy(sortConditions)
                .limit(request.getSize() + 1)
                .fetch();
    }
}

