package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.OrderCreateDto;
import hello.matdil.domain.order.dto.OrderItemCreateDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.event.OrderCreateEvent;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.event.GenericEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final GenericEventPublisher genericEventPublisher;

    public void createOrder(OrderCreateDto orderCreateDto) {
        Order order = Order.builder()
                .userId(orderCreateDto.userId())
                .storeId(orderCreateDto.storeId())
                .orderItems(orderCreateDto.orderItemCreateDtoList().stream()
                        .map(item -> new OrderItem(item.menuId(), item.quantity(), item.price()))
                        .toList())
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(orderCreateDto.orderItemCreateDtoList().stream()
                        .mapToInt(OrderItemCreateDto::price).sum())
                .requestNote(orderCreateDto.requestNote())
                .expectedDeliveryTime(orderCreateDto.expectedDeliveryTime())
                .build();

        orderRepository.save(order);

        genericEventPublisher.publish(new OrderCreateEvent(order.getId(), order.getUserId()));
    }

}
