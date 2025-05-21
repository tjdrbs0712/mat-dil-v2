package hello.matdil.domain.order.dto;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponseDto {

    private Long orderId;
    private Long storeId;
    private Long userId;
    private OrderStatus orderStatus;
    private int totalPrice;
    private String requestNote;
    private LocalDateTime expectedDeliveryTime;
    private LocalDateTime createdAt;

    private List<OrderItemDto> orderItems;

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .storeId(order.getStoreId())
                .userId(order.getUserId())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .requestNote(order.getRequestNote())
                .expectedDeliveryTime(order.getExpectedDeliveryTime())
                .createdAt(order.getCreatedAt())
                .orderItems(
                        order.getOrderItems().stream()
                                .map(OrderItemDto::from)
                                .toList()
                )
                .build();
    }

    @Getter
    @Builder
    public static class OrderItemDto {
        private Long menuId;
        private int quantity;
        private int price;

        public static OrderItemDto from(OrderItem item) {
            return OrderItemDto.builder()
                    .menuId(item.getMenuId())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();
        }
    }
}