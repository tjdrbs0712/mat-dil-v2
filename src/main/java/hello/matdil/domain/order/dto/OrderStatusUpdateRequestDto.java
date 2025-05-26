package hello.matdil.domain.order.dto;

import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.global.validator.EnumValid;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusUpdateRequestDto {

    @EnumValid(message = "정확한 값을 입력해주세요.", enumClass = OrderStatus.class)
    private String status;

    public OrderStatus toEnum() {
        return OrderStatus.valueOf(status.toUpperCase());
    }
}