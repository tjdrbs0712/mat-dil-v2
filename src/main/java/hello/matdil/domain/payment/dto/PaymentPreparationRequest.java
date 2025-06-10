package hello.matdil.domain.payment.dto;

import hello.matdil.domain.payment.entity.PaymentMethod;
import hello.matdil.global.validator.EnumValid;

public record PaymentPreparationRequest(
        Long orderId,
        String pg,

        @EnumValid(message = "결제 유형을 제대로 입력해 주세요.", enumClass = PaymentMethod.class)
        String method
) {
    public PaymentMethod getMethod(){
            return PaymentMethod.valueOf(method.toUpperCase());
    }
}