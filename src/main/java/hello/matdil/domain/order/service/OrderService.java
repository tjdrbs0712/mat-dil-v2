package hello.matdil.domain.order.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.entity.DeliveryStatus;
import hello.matdil.domain.delivery.repository.DeliveryRepository;
import hello.matdil.domain.order.dto.OrderCreateDto;
import hello.matdil.domain.order.dto.OrderItemCreateDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;

    void createOrder(OrderCreateDto orderCreateDto){
        Order order = Order.builder()
                .userId(orderCreateDto.userId())
                .storeId(orderCreateDto.storeId())
                .orderItems(orderCreateDto.orderItemCreateDtoList().stream()
                        .map(item -> new OrderItem(item.menuId(), item.quantity(), item.price()))
                        .toList())
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(orderCreateDto.orderItemCreateDtoList().stream().mapToInt(OrderItemCreateDto::price).sum())
                .requestNote(orderCreateDto.requestNote())
                .expectedDeliveryTime(orderCreateDto.expectedDeliveryTime())
                .build();

        orderRepository.save(order);

        User user = userRepository.findById(orderCreateDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        Address address = user.getAddress();

        Delivery delivery = Delivery.builder()
                .orderId(order.getId())
                .address(address)
                .deliveryStatus(DeliveryStatus.READY)
                .build();

        deliveryRepository.save(delivery);
    }

}
