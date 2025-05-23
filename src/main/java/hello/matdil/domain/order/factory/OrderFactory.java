package hello.matdil.domain.order.factory;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderFactory {

    public Order create(Long userId,
                        Long storeId,
                        LocalDateTime expectedDeliveryTime,
                        String requestNote,
                        List<OrderItem> orderItems) {

        Order order = Order.builder()
                .userId(userId)
                .storeId(storeId)
                .orderStatus(OrderStatus.CREATED)
                .expectedDeliveryTime(expectedDeliveryTime)
                .requestNote(requestNote)
                .orderItems(orderItems)
                .build();

        for (OrderItem item : orderItems) {
            item.assignOrder(order);
        }

        order.setTotalPrice(
                orderItems.stream()
                        .mapToInt(OrderItem::getPrice)
                        .sum()
        );

        return order;
    }
}
