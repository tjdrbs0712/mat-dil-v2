package hello.matdil.domain.payment.validator;

import hello.matdil.domain.payment.entity.PaymentStatus;
import hello.matdil.domain.payment.exception.PaymentErrorCode;
import hello.matdil.domain.payment.exception.PaymentException;
import hello.matdil.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentValidator {

    private final PaymentRepository paymentRepository;

    public void existsByIdAndStatusComplete(Long orderId, PaymentStatus status){
        if (paymentRepository.existsByOrderIdAndStatus(orderId, status)) {
            throw new PaymentException(PaymentErrorCode.ALREADY_COMPLETED);
        }
    }

}
