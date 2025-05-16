package hello.matdil.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Store> findStoresByCondition(String address, String name, String sort, int page, int size) {
        QStore store = QStore.store;

        BooleanBuilder builder = buildWhere(address, name, store);

        StoreSortType sortType = StoreSortType.from(sort);
        OrderSpecifier<?> sortCondition = sortType.toOrderSpecifier(store);

        List<Store> result = queryFactory
                .selectFrom(store)
                .where(builder)
                .orderBy(sortCondition)
                .offset((long) page * size)
                .limit(size + 1)
                .fetch();

        boolean hasNext = result.size() > size;
        if (hasNext) result.remove(size);

        return new SliceImpl<>(result, PageRequest.of(page, size), hasNext);
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

