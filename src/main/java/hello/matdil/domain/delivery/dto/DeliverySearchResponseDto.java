package hello.matdil.domain.delivery.dto;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.entity.DeliveryStatus;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;

import java.math.BigDecimal;

public record DeliverySearchResponseDto(
        Long deliveryId,
        Long orderId,
        String storeName,
        Address address,
        BigDecimal deliveryFee,
        DeliveryStatus deliveryStatus
) {

    public static DeliverySearchResponseDto from(Delivery delivery, StoreSummaryResponseDto dto) {
        return new DeliverySearchResponseDto(
                delivery.getId(),
                delivery.getOrderId(),
                dto.getName(),
                dto.getAddress(),
                delivery.getDeliveryFee(),
                delivery.getDeliveryStatus()
        );
    }

    public static DeliverySearchResponseDto fromCache(DeliveryCacheDto cacheDto, StoreSummaryResponseDto dto) {
        return new DeliverySearchResponseDto(
                cacheDto.deliveryId(),
                cacheDto.orderId(),
                dto.getName(),
                dto.getAddress(),
                cacheDto.deliveryFee(),
                cacheDto.deliveryStatus()
        );
    }
}