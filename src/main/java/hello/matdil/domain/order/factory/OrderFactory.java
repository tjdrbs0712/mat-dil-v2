package hello.matdil.domain.order.factory;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.address.AddressFactory;
import hello.matdil.domain.order.dto.OrderCreateRequestDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFactory {

    private final AddressFactory addressFactory;

    public Order create(Long userId,
                        Long storeId,
                        LocalDateTime expectedDeliveryTime,
                        String requestNote,
                        List<OrderItem> orderItems,
                        OrderCreateRequestDto.AddressDto addressDto) {

        Address address = addressFactory.create(addressDto.city(), addressDto.street(), addressDto.detailAddress());

        Order order = Order.builder()
                .userId(userId)
                .storeId(storeId)
                .orderStatus(OrderStatus.CREATED)
                .expectedDeliveryTime(expectedDeliveryTime)
                .requestNote(requestNote)
                .orderItems(orderItems)
                .deliveryAddress(address)
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
