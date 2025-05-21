package hello.matdil.domain.order.service;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, Long id, LocalDateTime expectedDeliveryTime
            , String requestNote, List<OrderItem> orderItems);
}
