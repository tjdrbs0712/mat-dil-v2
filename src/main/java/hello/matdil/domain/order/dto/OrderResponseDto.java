package hello.matdil.domain.order.dto;

import hello.matdil.domain.order.dto.cache.OrderCacheDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public record OrderResponseDto(
        Long orderId,
        Long storeId,
        Long userId,
        String storeName,
        String storeImageUrl,
        OrderStatus orderStatus,
        int totalPrice,
        String requestNote,
        LocalDateTime expectedDeliveryTime,
        LocalDateTime createdAt,
        List<OrderItemResponseDto> orderItems
) {
    public static OrderResponseDto from(Order order, StoreSummaryResponseDto storeInfo) {
        return new OrderResponseDto(
                order.getId(),
                order.getStoreId(),
                order.getUserId(),
                storeInfo.getName(),
                storeInfo.getImageUrl(),
                order.getOrderStatus(),
                order.getTotalPrice(),
                order.getRequestNote(),
                order.getExpectedDeliveryTime(),
                order.getCreatedAt(),
                order.getOrderItems().stream()
                        .map(OrderItemResponseDto::from)
                        .toList()
        );
    }

    public static OrderResponseDto from(OrderCacheDto cacheDto) {
        return new OrderResponseDto(
                cacheDto.orderId(),
                cacheDto.storeId(),
                cacheDto.userId(),
                cacheDto.storeName(),
                null, // 캐시에는 storeImageUrl이 없으므로 null 또는 기본값 처리
                cacheDto.orderStatus(),
                cacheDto.totalPrice(),
                cacheDto.requestNote(),
                LocalDateTime.ofEpochSecond(cacheDto.expectedDeliveryTimeEpoch(), 0, ZoneOffset.UTC),
                LocalDateTime.ofEpochSecond(cacheDto.createdAtEpoch(), 0, ZoneOffset.UTC),
                cacheDto.orderItems().stream()
                        .map(itemCache -> new OrderItemResponseDto(itemCache.menuId(), itemCache.quantity(), itemCache.price()))
                        .toList()
        );
    }
}