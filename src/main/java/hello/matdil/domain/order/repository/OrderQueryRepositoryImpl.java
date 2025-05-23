package hello.matdil.domain.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.order.dto.OrderCursorRequestDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.entity.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> findByIdWithNotDeleted(Long orderId) {
        QOrder order = QOrder.order;

        return Optional.ofNullable(queryFactory
                .selectFrom(order)
                .where(order.id.eq(orderId),
                        order.orderStatus.ne(OrderStatus.DELETED))
                .fetchOne());
    }

    @Override
    public List<Order> findOrdersByUserIdWithCursor(Long userId, OrderCursorRequestDto cursor) {
        QOrder order = QOrder.order;

        BooleanExpression baseCondition = order.userId.eq(userId)
                .and(order.orderStatus.ne(OrderStatus.DELETED));

        BooleanExpression cursorCondition = null;
        if (cursor.hasCursor()) {
            cursorCondition = order.createdAt.lt(cursor.lastCreatedAt())
                    .or(order.createdAt.eq(cursor.lastCreatedAt())
                            .and(order.id.lt(cursor.lastOrderId())));
        }

        return queryFactory
                .selectFrom(order)
                .where(baseCondition, cursorCondition)
                .orderBy(order.createdAt.desc(), order.id.desc())
                .limit(cursor.pageSize() + 1)
                .fetch();
    }
}
