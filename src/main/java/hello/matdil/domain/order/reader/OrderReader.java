package hello.matdil.domain.order.reader;

import hello.matdil.domain.order.dto.OrderCursorRequestDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderReader {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public Order getOrderWithPermission(Long orderId, Long userId, UserRole role) {
        Order order = orderRepository.findByIdWithNotDeleted(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        order.validateAccessibleTo(userId, role);
        return order;
    }

    public List<Order> getOrdersByUserIdWithCursor(Long userId, OrderCursorRequestDto cursor) {
        return orderRepository.findOrdersByUserIdWithCursor(userId, cursor);
    }

    public List<Order> getOrdersByStoreIdWithCursor(Long storeId, OrderCursorRequestDto cursor) {
        return orderRepository.findOrdersByUserIdWithCursor(storeId, cursor);
    }

    public Order getOwnerOrderWithPermission(Long orderId, Long userId, UserRole role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
        Store store = storeRepository.findByIdWithNotDeleted(order.getStoreId())
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        store.validateAccessibleTo(userId, role);
        return order;
    }
}