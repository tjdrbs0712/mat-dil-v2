package hello.matdil.domain.order.dto;

import hello.matdil.domain.order.entity.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemResponseDto {
    private Long menuId;
    private int quantity;
    private int price;

    public static OrderItemResponseDto from(OrderItem item) {
        return OrderItemResponseDto.builder()
                .menuId(item.getMenuId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}
