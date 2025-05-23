package hello.matdil.domain.order.dto;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.store.dto.StoreInfoDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSummaryDto {
    private Long orderId;
    private Long storeId;
    private String storeName;
    private String storeImageUrl;
    private OrderStatus orderStatus;
    private int totalPrice;
    private LocalDateTime createdAt;

    public static OrderSummaryDto from(Order order, StoreInfoDto storeInfoDto) {
        return OrderSummaryDto.builder()
                .orderId(order.getId())
                .storeId(order.getStoreId())
                .storeName(storeInfoDto.name())
                .storeImageUrl(storeInfoDto.imageUrl())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
