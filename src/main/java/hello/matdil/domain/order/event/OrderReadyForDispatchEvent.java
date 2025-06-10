package hello.matdil.domain.order.event;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.order.entity.Order;

import java.math.BigDecimal;

public record OrderReadyForDispatchEvent(
        Long orderId,
        BigDecimal deliveryFee,
        Address address
) {
    public static OrderReadyForDispatchEvent from(Order order) {
        return new OrderReadyForDispatchEvent(
                order.getId(),
                BigDecimal.valueOf(order.getTotalPrice()),
                order.getDeliveryAddress()
        );
    }

}
