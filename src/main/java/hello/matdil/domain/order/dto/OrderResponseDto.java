package hello.matdil.domain.order.dto;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.store.dto.StoreInfoDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponseDto {

    private Long orderId;
    private Long storeId;
    private Long userId;
    private String storeName;
    private String storeImageUrl;
    private OrderStatus orderStatus;
    private int totalPrice;
    private String requestNote;
    private LocalDateTime expectedDeliveryTime;
    private LocalDateTime createdAt;

    private List<OrderItemResponseDto> orderItems;

    public static OrderResponseDto from(Order order, StoreInfoDto storeSummary) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .storeId(order.getStoreId())
                .userId(order.getUserId())
                .storeName(storeSummary.name())
                .storeImageUrl(storeSummary.imageUrl())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .requestNote(order.getRequestNote())
                .expectedDeliveryTime(order.getExpectedDeliveryTime())
                .createdAt(order.getCreatedAt())
                .orderItems(
                        order.getOrderItems().stream()
                                .map(OrderItemResponseDto::from)
                                .toList()
                )
                .build();
    }
}