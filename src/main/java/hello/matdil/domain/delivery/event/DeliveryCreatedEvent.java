package hello.matdil.domain.delivery.event;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.entity.Delivery;

import java.math.BigDecimal;

public record DeliveryCreatedEvent(
        Long deliveryId,
        Long orderId,
        BigDecimal deliveryFee,
        Address address
) {
    public static DeliveryCreatedEvent from(Delivery delivery) {
        return new DeliveryCreatedEvent(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getDeliveryFee(),
                delivery.getAddress()
        );
    }
}
