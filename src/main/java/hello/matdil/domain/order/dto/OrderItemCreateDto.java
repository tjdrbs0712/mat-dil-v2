package hello.matdil.domain.order.dto;

public record OrderItemCreateDto(
        Long menuId,
        int quantity,
        int price
){}
