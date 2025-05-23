package hello.matdil.domain.order.reader;

import hello.matdil.domain.order.dto.OrderCursorRequestDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderReader {

    private final OrderRepository orderRepository;

    public Order getOrderWithPermission(Long orderId, Long userId, UserRole role) {
        Order order = orderRepository.findByIdWithNotDeleted(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        order.validateAccessibleTo(userId, role);
        return order;
    }

    public List<Order> getOrdersByUserIdWithCursor(Long userId, OrderCursorRequestDto cursor) {
        return orderRepository.findOrdersByUserIdWithCursor(userId, cursor);
    }
}