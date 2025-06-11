package hello.matdil.domain.order.dto.cache;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;

import java.time.ZoneOffset;
import java.util.List;

public record OrderCacheDto(
        Long orderId,
        Long storeId,
        Long userId,
        String storeName,
        OrderStatus orderStatus,
        int totalPrice,
        String requestNote,
        long expectedDeliveryTimeEpoch,
        long createdAtEpoch,
        List<OrderItemCacheDto> orderItems
)  {
    public static OrderCacheDto from(Order order, StoreSummaryResponseDto storeInfo) {
        return new OrderCacheDto(
                order.getId(),
                order.getStoreId(),
                order.getUserId(),
                storeInfo.getName(),
                order.getOrderStatus(),
                order.getTotalPrice(),
                order.getRequestNote(),
                order.getExpectedDeliveryTime().toEpochSecond(ZoneOffset.UTC),
                order.getCreatedAt().toEpochSecond(ZoneOffset.UTC),
                order.getOrderItems().stream()
                        .map(OrderItemCacheDto::from)
                        .toList()
        );
    }
}
