package hello.matdil.domain.payment.pg;

import hello.matdil.domain.payment.entity.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PgProviderSelector {
    public String determinePgProvider(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case KAKAO_PAY -> "tosspayments.kakaopay";
            case NAVER_PAY -> "tosspayments.naverpay";
            case TOSS_PAY -> "tosspayments.tosspay";
            default -> "tosspayments.card";
        };
    }
}
