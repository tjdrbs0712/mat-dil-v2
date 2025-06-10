package hello.matdil.domain.store.menu.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.store.menu.dto.MenuCursorRequestDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.entity.MenuStatus;
import hello.matdil.domain.store.menu.entity.QMenu;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MenuQueryRepositoryImpl implements MenuQueryRepository{

    private final JPAQueryFactory queryFactory;

    public List<Menu> findMenusByCursor(Long userId, UserRole role, Long storeId, MenuCursorRequestDto cursor) {
        QMenu menu = QMenu.menu;

        BooleanExpression baseCondition = menu.store.id.eq(storeId)
                .and(buildVisibilityFilter(userId, role, menu));

        BooleanExpression cursorCondition = null;

        if (cursor.hasCursor()) {
            cursorCondition = menu.orderIndex.gt(cursor.lastOrderIndex())
                    .or(menu.orderIndex.eq(cursor.lastOrderIndex())
                            .and(menu.id.lt(cursor.lastMenuId())));
        }

        return queryFactory
                .selectFrom(menu)
                .where(baseCondition, cursorCondition)
                .orderBy(menu.orderIndex.asc(), menu.id.desc())
                .limit(cursor.pageSize() + 1)
                .fetch();
    }

    private BooleanExpression buildVisibilityFilter(Long userId, UserRole role, QMenu menu) {
        return switch (role) {
            case USER -> menu.menuStatus.in(MenuStatus.AVAILABLE, MenuStatus.SOLD_OUT);

            case OWNER -> menu.menuStatus.in(MenuStatus.AVAILABLE, MenuStatus.SOLD_OUT)
                    .or(
                            menu.menuStatus.eq(MenuStatus.HIDDEN)
                                    .and(menu.store.ownerId.eq(userId))
                    );

            case ADMIN -> Expressions.TRUE;
        };
    }

    public Optional<Menu> findByIdWithStoreFetchJoinNotDeleted(Long menuId, Long storeId) {
        QMenu menu = QMenu.menu;
        QStore store = QStore.store;

        Menu result = queryFactory
                .selectFrom(menu)
                .join(menu.store, store).fetchJoin()
                .where(
                        menu.id.eq(menuId),
                        store.id.eq(storeId),
                        store.status.ne(StoreStatus.DELETED)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public Optional<Menu> findByIdWithStore(Long menuId, Long storeId) {
        QMenu menu = QMenu.menu;

        Menu result = queryFactory
                .selectFrom(menu)
                .where(
                        menu.id.eq(menuId),
                        menu.store.id.eq(storeId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Menu> findAllByStoreIdAndIdIn(Long storeId, List<Long> menuIds) {

        QMenu menu = QMenu.menu;

        return queryFactory
                .selectFrom(menu)
                .where(
                        menu.store.id.eq(storeId),
                        menu.id.in(menuIds)
                )
                .fetch();
    }

}