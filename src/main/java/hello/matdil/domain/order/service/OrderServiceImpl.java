package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.OrderResponseDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order createOrder(Long userId, Long storeId, LocalDateTime expectedDeliveryTime,
                             String requestNote, List<OrderItem> orderItems) {

        Order order = Order.create(userId, storeId, expectedDeliveryTime, requestNote, orderItems);
        return orderRepository.save(order);
    }
}
