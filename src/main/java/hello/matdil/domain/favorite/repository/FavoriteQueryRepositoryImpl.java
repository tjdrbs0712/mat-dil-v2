package hello.matdil.domain.favorite.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteStoreSummaryDto;
import hello.matdil.domain.favorite.entity.QFavorite;
import hello.matdil.domain.favorite.sort.FavoriteSortStrategy;
import hello.matdil.domain.favorite.sort.FavoriteSortStrategyFactory;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.userorderstatus.entity.QUserOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FavoriteQueryRepositoryImpl implements FavoriteQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final FavoriteSortStrategyFactory sortStrategyFactory;

    @Override
    public List<FavoriteStoreSummaryDto> loadFavoriteStoreSummaries(Long userId, FavoriteCursorRequestDto dto) {
        QFavorite favorite = QFavorite.favorite;
        QStore store = QStore.store;
        QUserOrderStatus status = QUserOrderStatus.userOrderStatus;

        FavoriteSortStrategy strategy = sortStrategyFactory.getStrategy(dto.getSortType());
        Map<String, Object> cursor = dto.toCursorParamMap();

        List<Tuple> result = queryFactory
                .select(store, status.orderCount, status.lastOrderedAt)
                .from(favorite)
                .join(store).on(store.id.eq(favorite.storeId))
                .leftJoin(status).on(
                        status.userId.eq(userId)
                                .and(status.storeId.eq(favorite.storeId))
                )
                .where(favorite.userId.eq(userId)
                        .and(strategy.buildCursorPredicate(store, status, cursor)))
                .orderBy(strategy.getOrderSpecifiers(store, status))
                .limit(dto.pageSize() + 1)
                .fetch();

        return result.stream()
                .map(tuple -> {
                    Store s = tuple.get(store);
                    Integer orderCount = tuple.get(status.orderCount);
                    LocalDateTime lastOrderedAt = tuple.get(status.lastOrderedAt);
                    return FavoriteStoreSummaryDto.from(s, orderCount, lastOrderedAt);
                })
                .toList();
    }
}
