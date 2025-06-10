package hello.matdil.domain.order.dto.cache;

import hello.matdil.domain.order.entity.OrderItem;

public record OrderItemCacheDto(
        Long menuId,
        int quantity,
        int price
) {
    public static OrderItemCacheDto from(OrderItem orderItem) {
        return new OrderItemCacheDto(
                orderItem.getMenuId(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }
}