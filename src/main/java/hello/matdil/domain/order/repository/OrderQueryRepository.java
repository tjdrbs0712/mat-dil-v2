package hello.matdil.domain.order.repository;

import hello.matdil.domain.order.dto.OrderCursorRequestDto;
import hello.matdil.domain.order.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {
    Optional<Order> findByIdWithNotDeleted(Long orderId);

    List<Order> findOrdersByUserIdWithCursor(Long userId, OrderCursorRequestDto cursor);

    List<Order> findOrdersByStoreIdWithCursor(Long storeId, OrderCursorRequestDto cursor);
}
