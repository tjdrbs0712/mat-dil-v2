package hello.matdil.domain.delivery.dto;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.entity.DeliveryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record DeliveryCacheDto(
        Long deliveryId,
        Long orderId,
        Long riderId,
        DeliveryStatus deliveryStatus,
        BigDecimal deliveryFee,
        AddressDto address,
        long createdAtEpoch,
        long assignedAtEpoch,
        long pickedUpAtEpoch,
        long deliveredAtEpoch
) {
    public record AddressDto(
            String city,
            String street,
            String detailAddress,
            double latitude,
            double longitude
    ) {
    }

    public static DeliveryCacheDto from(Delivery delivery) {
        Address address = delivery.getAddress();
        return new DeliveryCacheDto(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getRiderId(),
                delivery.getDeliveryStatus(),
                delivery.getDeliveryFee(),
                new AddressDto(
                        address.getCity(),
                        address.getStreet(),
                        address.getDetailAddress(),
                        address.getLatitude(),
                        address.getLongitude()
                ),
                toEpochSecond(delivery.getCreatedAt()),
                toEpochSecond(delivery.getAssignedTime()),
                toEpochSecond(delivery.getPickedUpAt()),
                toEpochSecond(delivery.getDeliveredAt())
        );
    }

    private static long toEpochSecond(LocalDateTime ldt) {
        return ldt != null ? ldt.toEpochSecond(ZoneOffset.UTC) : 0;
    }
}
