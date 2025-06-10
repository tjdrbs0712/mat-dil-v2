package hello.matdil.domain.order.dto;

import hello.matdil.domain.order.entity.OrderItem;

public record OrderItemResponseDto(
        Long menuId,
        int quantity,
        int price
) {
    // OrderItem 엔티티를 DTO로 변환하는 정적 팩토리 메서드
    public static OrderItemResponseDto from(OrderItem orderItem) {
        return new OrderItemResponseDto(
                orderItem.getMenuId(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }
}