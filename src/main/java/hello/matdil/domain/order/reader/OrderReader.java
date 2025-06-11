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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderReader {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public Order readWithUserPermission(Long orderId, Long userId, UserRole role) {
        Order order = readById(orderId);
        order.validateAccessibleTo(userId, role);
        return order;
    }

    @Transactional(readOnly = true)
    public Order readWithStorePermission(Long orderId, Long userId, UserRole role) {
        Order order = readById(orderId);
        Store store = readStoreWithNotDeleted(order.getStoreId());
        store.validateAccessibleTo(userId, role);
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> readByUserWithCursor(Long userId, OrderCursorRequestDto cursor) {
        return orderRepository.findOrdersByUserIdWithCursor(userId, cursor);
    }

    @Transactional(readOnly = true)
    public List<Order> readByStoreWithCursor(
            Long storeId, OrderCursorRequestDto cursor) {
        return orderRepository.findOrdersByStoreIdWithCursor(storeId, cursor);
    }

    public Order readById(Long orderId) {
        return orderRepository.findByIdWithNotDeleted(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
    }

    private Store readStoreWithNotDeleted(Long storeId) {
        return storeRepository.findByIdWithNotDeleted(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
    }

    public List<Order> findAllIn(List<Long> orderIds) {
        return orderRepository.findAllIdWithNotDeleted(orderIds);
    }
}
